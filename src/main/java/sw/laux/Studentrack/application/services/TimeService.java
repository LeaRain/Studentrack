package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.persistence.entities.TimeOrder;
import sw.laux.Studentrack.persistence.repository.TimeRepository;

@Service
public class TimeService implements ITimeService {
    @Autowired
    private TimeRepository timeRepo;

    @Override
    public TimeOrder findOpenTimeOrder(TimeOrder timeOrder) {
        var timeOrderMatch = timeRepo.findByEndIsNull(timeOrder);
        return null;

    }
}
