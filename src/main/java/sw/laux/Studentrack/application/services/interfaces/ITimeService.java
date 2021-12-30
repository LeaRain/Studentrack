package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.TimeDuration;
import sw.laux.Studentrack.persistence.entities.TimeOrder;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

public interface ITimeService {
    TimeOrder findOpenTimeOrderForStudent(Student student) throws StudentrackObjectNotFoundException;
    @Transactional
    TimeOrder createOpenTimeOrder(TimeOrder timeOrder) throws StudentrackObjectAlreadyExistsException;
    @Transactional
    TimeOrder closeOpenTimeOrderForStudent(TimeOrder timeOrder, Student student) throws StudentrackObjectNotFoundException;
    @Transactional
    Iterable<TimeOrder> getAllTimeOrdersForStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeOrder saveTimeOrder(TimeOrder timeOrder);
    TimeOrder findTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException;
    TimeOrder findTimeOrder(long timeOrderId) throws StudentrackObjectNotFoundException;
    TimeOrder updateTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException;
    @Transactional
    void deleteTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException;
    @Transactional
    Iterable<TimeOrder> findTimeOrdersForModuleAndStudent(Module module, Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForDateAndStudent(Date date, Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForTimeRangeAndStudent(Date start, Date end, Student student) throws StudentrackObjectNotFoundException;
    TimeDuration calculateTimeDuration(Iterable<TimeOrder> timeOrders);
    TimeDuration getTimeInvestDurationForTodayAndStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentWeekAndStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentMonthAndStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentYearAndStudent(Student student) throws StudentrackObjectNotFoundException;
    Date[] getWeekStartAndEnd();
    Date[] getMonthStartAndEnd();
    Date[] getYearStartAndEnd();
    Date prepareInputDate(Date date, boolean back);

    Date setTimeToMidnight(Date date);
}
