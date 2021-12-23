package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.TimeOrder;
import sw.laux.Studentrack.persistence.repository.TimeRepository;

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


}
