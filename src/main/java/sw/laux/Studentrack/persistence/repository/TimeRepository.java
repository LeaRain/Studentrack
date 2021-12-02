package sw.laux.Studentrack.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sw.laux.Studentrack.persistence.entities.TimeOrder;

@Repository
public interface TimeRepository extends CrudRepository<TimeOrder, Long> {
}