package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.persistence.entities.TimeOrder;

public interface ITimeService {
    TimeOrder findOpenTimeOrder(TimeOrder timeOrder);
}
