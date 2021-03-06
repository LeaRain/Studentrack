package sw.laux.Studentrack.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.TimeOrder;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TimeRepository extends CrudRepository<TimeOrder, Long> {
    Optional<TimeOrder> findByEndIsNullAndOwner(Student owner);
    Optional<Iterable<TimeOrder>> findAllByOwnerOrderByStartDesc(Student owner);
    Optional<Iterable<TimeOrder>> findAllByOwnerAndModule(Student owner, Module module);
    Optional<Iterable<TimeOrder>> findAllByOwnerAndStartBetween(Student owner, Date start, Date end);
    Optional<Iterable<TimeOrder>> findAllByModuleAndStartBetween(Module module, Date start, Date end);
    Optional<Iterable<TimeOrder>> findAllByStartBetween(Date start, Date end);
    Optional<Iterable<TimeOrder>> findAllByModule(Module module);
    Optional<Iterable<TimeOrder>> findAllByOwner(Student owner);
}
