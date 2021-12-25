package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.TimeOrder;

public interface ITimeService {
    TimeOrder findOpenTimeOrderForStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeOrder createOpenTimeOrder(TimeOrder timeOrder) throws StudentrackObjectAlreadyExistsException;
    TimeOrder closeOpenTimeOrderForStudent(TimeOrder timeOrder, Student student) throws StudentrackObjectNotFoundException;
    Iterable<TimeOrder> getAllTimeOrdersForStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeOrder saveTimeOrder(TimeOrder timeOrder);
    TimeOrder findTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException;
    TimeOrder findTimeOrder(long timeOrderId) throws StudentrackObjectNotFoundException;
    TimeOrder updateTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException;
    void deleteTimeOrder(TimeOrder timeOrder) throws StudentrackObjectNotFoundException;
}