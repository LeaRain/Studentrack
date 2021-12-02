package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import sw.laux.Studentrack.persistence.repository.ITimeRepo;

public class TimeService {
    @Autowired
    private ITimeRepo timeRepo;
}
