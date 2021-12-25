package sw.laux.Studentrack.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.TimeOrder;

import java.util.Optional;

@Repository
public interface TimeRepository extends CrudRepository<TimeOrder, Long> {
    Optional<TimeOrder> findByEndIsNullAndOwner(Student owner);
    Optional<Iterable<TimeOrder>> findAllByOwnerOrderByStartDesc(Student owner);
}
