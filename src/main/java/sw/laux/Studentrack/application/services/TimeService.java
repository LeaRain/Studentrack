package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.persistence.repository.TimeRepository;

@Service
public class TimeService {
    @Autowired
    private TimeRepository timeRepo;
}
