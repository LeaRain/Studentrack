package sw.laux.Studentrack.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import sw.laux.Studentrack.persistence.entities.TimeOrder;

public interface ITimeRepo extends CrudRepository<TimeOrder, Long> {
}
