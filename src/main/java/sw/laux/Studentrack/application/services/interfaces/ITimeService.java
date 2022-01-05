package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.exceptions.StudentrackOperationNotAllowedException;
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
    TimeOrder createOpenTimeOrder(TimeOrder timeOrder) throws StudentrackObjectAlreadyExistsException, StudentrackOperationNotAllowedException;
    @Transactional
    TimeOrder closeOpenTimeOrderForStudent(TimeOrder timeOrder, Student student) throws StudentrackObjectNotFoundException;
    @Transactional
    Iterable<TimeOrder> getAllTimeOrdersForStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeOrder saveTimeOrder(TimeOrder timeOrder) throws StudentrackOperationNotAllowedException;
    TimeOrder findTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException;
    TimeOrder findTimeOrder(long timeOrderId) throws StudentrackObjectNotFoundException;
    TimeOrder updateTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException;
    @Transactional
    void deleteTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException;
    @Transactional
    void deleteAllTimeOrdersForModule(Module module) throws StudentrackObjectNotFoundException;
    @Transactional
    Iterable<TimeOrder> findTimeOrdersForModuleAndStudent(Module module, Student student) throws StudentrackObjectNotFoundException;
    Iterable<TimeOrder> findAllTimeOrdersForModule(Module module) throws StudentrackObjectNotFoundException;
    boolean timeOrdersForModuleAndStudentAllowed(Module module, Student student);
    TimeDuration getTimeInvestDurationForDateAndStudent(Date date, Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForTimeRangeAndStudent(Date start, Date end, Student student) throws StudentrackObjectNotFoundException;
    TimeDuration calculateTimeDuration(Iterable<TimeOrder> timeOrders);
    TimeDuration getTimeInvestDurationForTodayAndStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentWeekAndStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentMonthAndStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForDateAndModule(Date date, Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentYearAndStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForTimeRangeAndModule(Date start, Date end, Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForTodayAndModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTotalTimeInvestDurationForModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentWeekAndModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentMonthAndModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentYearAndModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTotalTimeInvestDurationForToday() throws StudentrackObjectNotFoundException;
    TimeDuration getTotalTimeInvestDurationForTimeRange(Date start, Date end) throws StudentrackObjectNotFoundException;
    TimeDuration getTotalTimeInvestDurationForCurrentWeek() throws StudentrackObjectNotFoundException;
    Date[] getWeekStartAndEnd();
    Date[] getMonthStartAndEnd();
    Date[] getYearStartAndEnd();
    Date prepareInputDate(Date date, boolean back);
    Date setTimeToMidnight(Date date);
}
