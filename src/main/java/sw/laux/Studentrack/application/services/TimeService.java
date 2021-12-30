package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.TimeDuration;
import sw.laux.Studentrack.persistence.entities.TimeOrder;
import sw.laux.Studentrack.persistence.repository.TimeRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class TimeService implements ITimeService {
    @Autowired
    private TimeRepository timeRepo;

    @Override
    public TimeOrder findOpenTimeOrderForStudent(Student student) throws StudentrackObjectNotFoundException {
        var timeOrderMatch = timeRepo.findByEndIsNullAndOwner(student);

        if (timeOrderMatch.isEmpty()) {
            throw new StudentrackObjectNotFoundException(TimeOrder.class, student);
        }

        return timeOrderMatch.get();
    }

    @Override
    public TimeOrder createOpenTimeOrder(TimeOrder timeOrder) throws StudentrackObjectAlreadyExistsException {
        try {
            findOpenTimeOrderForStudent(timeOrder.getOwner());
            throw new StudentrackObjectAlreadyExistsException(timeOrder.getClass(), timeOrder);
        } catch (StudentrackObjectNotFoundException ignored) {
        }

        return timeRepo.save(timeOrder);
    }

    @Override
    public TimeOrder closeOpenTimeOrderForStudent(TimeOrder timeOrder, Student student) throws StudentrackObjectNotFoundException{
        var expectedOpenTimeOrder = findOpenTimeOrderForStudent(student);

        if (timeOrder.getTimeOrderId() != expectedOpenTimeOrder.getTimeOrderId()) {
            throw new StudentrackObjectNotFoundException(timeOrder.getClass(), timeOrder);
        }

        expectedOpenTimeOrder.setEnd(timeOrder.getEnd());
        return timeRepo.save(expectedOpenTimeOrder);
    }

    @Override
    public Iterable<TimeOrder> getAllTimeOrdersForStudent(Student student) throws StudentrackObjectNotFoundException {
        var timeOrdersOptional = timeRepo.findAllByOwnerOrderByStartDesc(student);

        if (timeOrdersOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(TimeOrder.class, student);
        }

        return timeOrdersOptional.get();

    }

    @Override
    public TimeOrder saveTimeOrder(TimeOrder timeOrder) {
        return timeRepo.save(timeOrder);
    }

    @Override
    public TimeOrder findTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException {
        return findTimeOrder(timeOrder.getTimeOrderId());
    }

    @Override
    public TimeOrder findTimeOrder(long timeOrderId) throws StudentrackObjectNotFoundException {
        var timeOrderOptional = timeRepo.findById(timeOrderId);

        if (timeOrderOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(TimeOrder.class, timeOrderId);
        }

        return timeOrderOptional.get();
    }

    @Override
    public TimeOrder updateTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException {
        findTimeOrder(timeOrder);
        return timeRepo.save(timeOrder);
    }

    @Override
    public void deleteTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException {
        findTimeOrder(timeOrder);
        timeRepo.delete(timeOrder);
    }

    @Override
    public Iterable<TimeOrder> findTimeOrdersForModuleAndStudent(Module module, Student student) throws StudentrackObjectNotFoundException {
        var timeOrderOptional = timeRepo.findAllByOwnerAndModule(student, module);

        if (timeOrderOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(TimeOrder.class, module);
        }

        return timeOrderOptional.get();
    }

    @Override
    public TimeDuration getTimeInvestDurationForDateAndStudent(Date date, Student student) throws StudentrackObjectNotFoundException {
        return getTimeInvestDurationForTimeRangeAndStudent(date, date, student);
    }

    @Override
    public TimeDuration getTimeInvestDurationForTimeRangeAndStudent(Date start, Date end, Student student) throws StudentrackObjectNotFoundException {
        start = prepareInputDate(start, true);
        end = prepareInputDate(end, false);
        System.out.println(start);
        System.out.println(end);

        var timeOrderOptional = timeRepo.findAllByOwnerAndStartBetween(student, start, end);
        //var timeOrderOptional = timeRepo.getAllBetweenStartAndEnd(start, end, student);
        if (timeOrderOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(TimeOrder.class, student);
        }

        var timeOrders = timeOrderOptional.get();

        System.out.println(timeOrders);

        return calculateTimeDuration(timeOrders);

    }

    @Override
    public TimeDuration calculateTimeDuration(Iterable<TimeOrder> timeOrders) {
        long durationSum = 0;
        for (var timeOrder : timeOrders) {
            var timeOrderDuration = timeOrder.getDuration();
            if (timeOrderDuration != null) {
                durationSum += timeOrderDuration.getDuration();
            }
        }

        var timeDuration = new TimeDuration();
        timeDuration.setDuration(durationSum);

        return timeDuration;
    }

    @Override
    public TimeDuration getTimeInvestDurationForTodayAndStudent(Student student) throws StudentrackObjectNotFoundException {
        return getTimeInvestDurationForDateAndStudent(new Date(), student);
    }

    @Override
    public TimeDuration getTimeInvestDurationForCurrentWeekAndStudent(Student student) throws StudentrackObjectNotFoundException {
        var dates = getWeekStartAndEnd();
        return getTimeInvestDurationForTimeRangeAndStudent(dates[0], dates[1], student);
    }

    @Override
    public TimeDuration getTimeInvestDurationForCurrentMonthAndStudent(Student student) throws StudentrackObjectNotFoundException {
        var dates = getMonthStartAndEnd();
        return getTimeInvestDurationForTimeRangeAndStudent(dates[0], dates[1], student);
    }

    @Override
    public TimeDuration getTimeInvestDurationForCurrentYearAndStudent(Student student) throws StudentrackObjectNotFoundException {
        var dates = getYearStartAndEnd();
        return getTimeInvestDurationForTimeRangeAndStudent(dates[0], dates[1], student);
    }

    @Override
    public Date[] getWeekStartAndEnd() {
        var dateResult = new Date[2];

        var calendar = new GregorianCalendar();
        var dayDifference = (-1) * calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        calendar.roll(Calendar.DAY_OF_MONTH, dayDifference);
        var weekStart = calendar.getTime();
        dateResult[0] = weekStart;
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        var weekEnd = calendar.getTime();
        dateResult[1] = weekEnd;

        return dateResult;
    }

    @Override
    public Date[] getMonthStartAndEnd() {
        var dateResult = new Date[2];

        var calendar = new GregorianCalendar();
        var firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        var firstDate = calendar.getTime();
        dateResult[0] = firstDate;
        var lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        var lastDate = calendar.getTime();
        dateResult[1] = lastDate;

        return dateResult;
    }

    @Override
    public Date[] getYearStartAndEnd() {
        var dateResult = new Date[2];

        var calendar = new GregorianCalendar();
        var firstDay = calendar.getActualMinimum(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, firstDay);
        var firstDate = calendar.getTime();
        dateResult[0] = firstDate;
        var lastDay = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, lastDay);
        var lastDate = calendar.getTime();
        dateResult[1] = lastDate;

        return dateResult;
    }

    @Override
    public Date prepareInputDate(Date date, boolean back) {
        date = setTimeToMidnight(date);
        if (back) {
            date = new Date(date.getTime() - 1);
        }

        else {
            date = new Date(date.getTime() + 86400000);
        }
        return date;
    }

    @Override
    public Date setTimeToMidnight(Date date) {
        var calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }


}
