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
    Iterable<TimeOrder> findAllTimeOrdersForStudent(Student student) throws StudentrackObjectNotFoundException;
    boolean timeOrdersForModuleAndStudentAllowed(Module module, Student student);
    Iterable<TimeOrder> getAllByOwnerAndStartBetween(Student student, Date start, Date end) throws StudentrackObjectNotFoundException;
    Iterable<TimeOrder> getAllByModuleAndStartBetween(Module module, Date start, Date end) throws StudentrackObjectNotFoundException;
    Iterable<TimeOrder> getAllByStartAndEnd(Date start, Date end) throws StudentrackObjectNotFoundException;
}
