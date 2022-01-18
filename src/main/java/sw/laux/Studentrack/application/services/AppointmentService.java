package sw.laux.Studentrack.application.services;

import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import sw.helblingd.terminportalbackend.repository.entity.persistent.*;
import sw.helblingd.terminportalbackend.repository.entity.dto.*;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.exceptions.StudentrackOperationNotAllowedException;
import sw.laux.Studentrack.application.services.interfaces.IAppointmentService;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.TimeOrder;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Service
public class AppointmentService implements IAppointmentService {
    @Autowired
    private RestTemplate restServiceClient;
    @Autowired
    private ITimeService timeService;
    @Autowired
    private Logger logger;

    @Override
    public String getAuthenticationKey() throws StudentrackObjectNotFoundException {
        var node = restServiceClient.getForObject("http://localhost:7000/restapi/v1", TextNode.class);

        if (node == null) {
            throw new StudentrackObjectNotFoundException(String.class, ApiAuthenticationKey.class);
        }

        return node.textValue();
    }

    /*
    Short description of the idea:
    For every module, there is a number of credit hours per week.
    All credit hours are used for one date per week.
    This may seem unintuitive, but if anyone wants to change the date and the number of appointments per week, this can be achieved by using the appointment service directly.
    The idea of Studentrack is to enable a simple support for schedule and appointment planning and not the reimplementation of the service itself.
    So this functionality shows only the glimpse of the idea of interaction, as example of usage.
     */
    @Override
    public Schedule createScheduleBasedOnModule(Module module) throws StudentrackObjectNotFoundException {
        var lecturer = module.getResponsibleLecturer();
        var creditHours = module.getCreditHours();

        var recurringAppointment = new AppointmentDTO();
        var startingAppointment = new AppointmentDTO();

        startingAppointment.setStart(module.getStartDate());
        // calculate to milliseconds
        startingAppointment.setDuration(creditHours * 3600000L);
        startingAppointment.setSingleAppointment(true);
        var appointmentGroupDTO = new AppointmentGroupDTO();
        appointmentGroupDTO.setDescription(module.getDescription());
        appointmentGroupDTO.setName(module.getName());
        appointmentGroupDTO.setMaxMembers(100);
        appointmentGroupDTO.setLocation("Starfleet Academy");
        startingAppointment.setAppointmentGroup(appointmentGroupDTO);

        recurringAppointment.setFirstOccurrence(startingAppointment);
        // one week in milliseconds
        recurringAppointment.setRecurrenceOffset(604800000L);
        recurringAppointment.setOccurrenceCount((long) module.getAppointmentCount());
        recurringAppointment.setSingleAppointment(false);
        recurringAppointment.setDuration(startingAppointment.getDuration());
        recurringAppointment.setStart(startingAppointment.getStart());
        recurringAppointment.setAppointmentGroup(appointmentGroupDTO);

        AppointmentDTO[] appointments = new AppointmentDTO[1];
        appointments[0] = startingAppointment;
        return saveScheduleBasedOnModule(appointments, lecturer.getAppointmentServiceApiKey());
    }

    @Override
    public Schedule saveScheduleBasedOnModule(AppointmentDTO[] appointments, String apiKey) throws StudentrackObjectNotFoundException, HttpServerErrorException {
        var parameters = new ApiKeyAndAppointmentArray();
        parameters.setApiKey(apiKey);
        parameters.setAppointments(appointments);

        var headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Type", "application/json");
        var entity = new org.springframework.http.HttpEntity<>(parameters, headers);

        var scheduleDTO = restServiceClient.postForObject("http://localhost:7000/restapi/v1/schedules", entity, ScheduleDTO.class);

        if (scheduleDTO == null) {
            throw new StudentrackObjectNotFoundException(Schedule.class, appointments);
        }

        return parseScheduleDTOToSchedule(scheduleDTO);
    }

    @Override
    public void createTimeOrdersForEnrolling(Module module, Student student) throws StudentrackObjectNotFoundException, IOException {
        var schedule = findScheduleForModule(module);
        createTimeOrdersBasedOnSchedule(schedule, student, module);
    }

    @Override
    public Schedule findScheduleForModule(Module module) throws StudentrackObjectNotFoundException, IOException {
        var scheduleId = module.getScheduleId();

        if (scheduleId == 0) {
            throw new StudentrackObjectNotFoundException(Schedule.class, module);
        }

        var lecturer = module.getResponsibleLecturer();

        if (lecturer == null || lecturer.getAppointmentServiceApiKey() == null) {
            throw new StudentrackObjectNotFoundException(Schedule.class, lecturer);
        }

        var client = HttpClientBuilder.create().build();
        var entity = new StringEntity('"' + lecturer.getAppointmentServiceApiKey() + '"');
        var uriString = "http://localhost:7000/restapi/v1/schedules" + module.getScheduleId();
        var request = getHttpGetRequestWithBody(URI.create(uriString), entity);
        var response = client.execute(request);
        var responseEntity = response.getEntity();
        return parseHttpGetResponseToSchedule(responseEntity);
    }

    @Override
    public void createTimeOrdersBasedOnSchedule(Schedule schedule, Student student, Module module) {
        var appointments = schedule.getAppointments();
        var timeOrders = new ArrayList<TimeOrder>();
        for (var appointment : appointments) {
            timeOrders.addAll(createTimeOrderBasedOnAppointment(appointment));
        }

        for (var timeOrder : timeOrders) {
            timeOrder.setOwner(student);
            timeOrder.setModule(module);
            timeOrder.setDuration();
            try {
                timeService.saveTimeOrder(timeOrder);
            } catch (StudentrackOperationNotAllowedException e) {
                logger.info(e.getMessage());
            }
        }
    }

    @Override
    public List<TimeOrder> createTimeOrderBasedOnAppointment(Appointment appointment) {
        var timeOrders = new ArrayList<TimeOrder>();

        if (appointment instanceof RecurringAppointment) {
            timeOrders.addAll(createTimeOrderBasedOnRecurringAppointment((RecurringAppointment) appointment));
        }

        if (appointment instanceof SingleAppointment) {
            timeOrders.add(createTimeOrderBasedOnSingleAppointment((SingleAppointment) appointment));
        }

        return timeOrders;
    }

    @Override
    public TimeOrder createTimeOrderBasedOnSingleAppointment(SingleAppointment singleAppointment) {
        return calculateTimeOrderForStartAndDuration(singleAppointment.getStart(), singleAppointment.getDuration());
    }

    @Override
    public List<TimeOrder> createTimeOrderBasedOnRecurringAppointment(RecurringAppointment recurringAppointment) {
        var timeOrders = new ArrayList<TimeOrder>();
        var firstAppointment = recurringAppointment.getFirstOccurrence();

        if (!(firstAppointment instanceof SingleAppointment)) {
            // Return an empty list, because modules should be based on single appointments
            return timeOrders;
        }

        var singleAppointment = (SingleAppointment) firstAppointment;
        var duration = singleAppointment.getDuration();
        var count = recurringAppointment.getOccurrenceCount();
        var calendar = new GregorianCalendar();
        calendar.setTime(singleAppointment.getStart());

        for (var i = 0; i < count; i++) {
            calendar.add(Calendar.MILLISECOND, (int) (i * recurringAppointment.getRecurrenceOffset()));
            var start = calendar.getTime();
            var timeOrder = calculateTimeOrderForStartAndDuration(start, duration);
            timeOrders.add(timeOrder);
        }
        return timeOrders;
    }

    @Override
    public TimeOrder calculateTimeOrderForStartAndDuration(Date start, long duration) {
        var calendar = new GregorianCalendar();
        calendar.setTime(start);
        calendar.add(Calendar.MILLISECOND, (int) duration);
        var end = calendar.getTime();

        var timeOrder = new TimeOrder();
        timeOrder.setStart(start);
        timeOrder.setEnd(end);

        return timeOrder;
    }

    /*
    Is it smart to use a GET request with a request body?
    Well, it is definitely a corner case, so Studentrack uses an own HTTP client for some requests, because this cannot be achieved (easily) with REST templates.
     */
    @Override
    public HttpEntityEnclosingRequestBase getHttpGetRequestWithBody(URI uri, HttpEntity entity) {
        var request = new HttpEntityEnclosingRequestBase() {
            @Override
            public String getMethod() {
                return "GET";
            }
        };

        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        request.setURI(uri);
        request.setEntity(entity);
        return request;
    }

    @Override
    public Schedule parseHttpGetResponseToSchedule(HttpEntity response) throws IOException {
        var stringResult = EntityUtils.toString(response);
        var parser = new Gson();
        var scheduleDTO = parser.fromJson(stringResult, ScheduleDTO.class);
        return parseScheduleDTOToSchedule(scheduleDTO);
    }

    @Override
    public Schedule parseScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        var schedule = new Schedule();
        schedule.setName(scheduleDTO.getName());
        schedule.setDescription(scheduleDTO.getDescription());
        schedule.setViewType(scheduleDTO.getViewType());
        var categoryDTO = scheduleDTO.getCategory();
        var category = new ScheduleCategory();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        schedule.setCategory(category);
        var appointmentsDTO = scheduleDTO.getAppointments();
        var appointments = new HashSet<Appointment>();

        for (var appointmentDTO : appointmentsDTO) {
            var appointment = parseAppointmentDTOToAppointment(appointmentDTO);
            appointments.add(appointment);
        }

        schedule.setAppointments(appointments);

        return schedule;
    }

    @Override
    public Appointment parseAppointmentDTOToAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment;
        if (appointmentDTO.isSingleAppointment()) {
            appointment = new SingleAppointment();
            ((SingleAppointment) appointment).setDuration(appointmentDTO.getDuration());
            ((SingleAppointment) appointment).setStart(appointmentDTO.getStart());
        }

        else {
            appointment = new RecurringAppointment();
            ((RecurringAppointment) appointment).setRecurrenceOffset(appointmentDTO.getRecurrenceOffset());
            ((RecurringAppointment) appointment).setOccurrenceCount(appointmentDTO.getOccurrenceCount());
            ((RecurringAppointment) appointment).setFirstOccurrence(parseAppointmentDTOToAppointment(appointmentDTO.getFirstOccurrence()));
        }

        appointment.setViewType(appointmentDTO.getViewType());
        appointment.setJoinType(appointmentDTO.getJoinType());

        return appointment;
    }
}
