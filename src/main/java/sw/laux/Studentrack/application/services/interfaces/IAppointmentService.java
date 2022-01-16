package sw.laux.Studentrack.application.services.interfaces;

import com.fasterxml.jackson.databind.node.TextNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import sw.helblingd.terminportalbackend.repository.entity.Appointment;
import sw.helblingd.terminportalbackend.repository.entity.RecurringAppointment;
import sw.helblingd.terminportalbackend.repository.entity.Schedule;
import sw.helblingd.terminportalbackend.repository.entity.SingleAppointment;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.TimeOrder;
import sw.laux.Studentrack.persistence.entities.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IAppointmentService {
    String getAuthenticationKey() throws StudentrackObjectNotFoundException;
    Schedule createScheduleBasedOnModule(Module module) throws StudentrackObjectNotFoundException;
    Schedule saveScheduleBasedOnModule(Appointment[] appointments, String apiKey) throws StudentrackObjectNotFoundException;
    void createTimeOrdersForEnrolling(Module module, Student student) throws StudentrackObjectNotFoundException, IOException;
    Schedule findScheduleForModule(Module module) throws StudentrackObjectNotFoundException, IOException;
    void createTimeOrdersBasedOnSchedule(Schedule schedule, Student student, Module module);
    List<TimeOrder> createTimeOrderBasedOnAppointment(Appointment appointment);
    TimeOrder createTimeOrderBasedOnSingleAppointment(SingleAppointment singleAppointment);
    List<TimeOrder> createTimeOrderBasedOnRecurringAppointment(RecurringAppointment recurringAppointment);
    TimeOrder calculateTimeOrderForStartAndDuration(Date start, long duration);
    HttpEntityEnclosingRequestBase getHttpGetRequestWithBody(URI uri, HttpEntity entity);
    Schedule parseHttpGetResponseToSchedule(HttpEntity response) throws IOException;
}
