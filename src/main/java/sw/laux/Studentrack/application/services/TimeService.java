package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.exceptions.StudentrackOperationNotAllowedException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.repository.TimeRepository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
public class TimeService implements ITimeService {
    @Autowired
    private TimeRepository timeRepo;

    @Autowired
    private IModuleService moduleService;

    @Override
    public TimeOrder findOpenTimeOrderForStudent(Student student) throws StudentrackObjectNotFoundException {
        var timeOrderMatch = timeRepo.findByEndIsNullAndOwner(student);

        if (timeOrderMatch.isEmpty()) {
            throw new StudentrackObjectNotFoundException(TimeOrder.class, student);
        }

        return timeOrderMatch.get();
    }

    @Override
    public TimeOrder createOpenTimeOrder(TimeOrder timeOrder) throws StudentrackObjectAlreadyExistsException, StudentrackOperationNotAllowedException {
        var timeOrderAllowed = timeOrdersForModuleAndStudentAllowed(timeOrder.getModule(), timeOrder.getOwner());

        if (!timeOrderAllowed) {
            throw new StudentrackOperationNotAllowedException(timeOrder.getClass(), timeOrder);
        }

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
    public TimeOrder saveTimeOrder(TimeOrder timeOrder) throws StudentrackOperationNotAllowedException {
        if (!(timeOrdersForModuleAndStudentAllowed(timeOrder.getModule(), timeOrder.getOwner()))) {
            throw new StudentrackOperationNotAllowedException(timeOrder.getClass(), timeOrder);
        }
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
    public void deleteAllTimeOrdersForModule(Module module) throws StudentrackObjectNotFoundException {
        var timeOrders = findAllTimeOrdersForModule(module);
        for (var timeOrder : timeOrders) {
            deleteTimeOrder(timeOrder);
        }
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
    public Iterable<TimeOrder> findAllTimeOrdersForModule(Module module) throws StudentrackObjectNotFoundException {
        var timeOrdersOptional = timeRepo.findAllByModule(module);

        if (timeOrdersOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(TimeOrder.class, module);
        }

        return timeOrdersOptional.get();
    }

    @Override
    public boolean timeOrdersForModuleAndStudentAllowed(Module module, Student student) {
        // Not passed -> Time Orders allowed
        return !(moduleService.hasStudentPassedModule(student, module));
    }

    @Override
    public Iterable<TimeOrder> getAllByOwnerAndStartBetween(Student student, Date start, Date end) throws StudentrackObjectNotFoundException {
        var timeOrderOptional = timeRepo.findAllByOwnerAndStartBetween(student, start, end);
        if (timeOrderOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(TimeOrder.class, student);
        }

         return timeOrderOptional.get();
    }

    @Override
    public Iterable<TimeOrder> getAllByModuleAndStartBetween(Module module, Date start, Date end) throws StudentrackObjectNotFoundException {
        var timeOrderOptional = timeRepo.findAllByModuleAndStartBetween(module, start, end);
        if (timeOrderOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(TimeOrder.class, module);
        }

        return timeOrderOptional.get();
    }

    @Override
    public Iterable<TimeOrder> getAllByStartAndEnd(Date start, Date end) throws StudentrackObjectNotFoundException {
        var timeOrderOptional = timeRepo.findAllByStartBetween(start, end);
        if (timeOrderOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(TimeOrder.class, new TimeOrder());
        }

        return timeOrderOptional.get();
    }


}
