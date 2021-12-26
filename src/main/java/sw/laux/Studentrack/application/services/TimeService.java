package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.persistence.entities.Module;
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


}
