package sw.laux.Studentrack.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sw.laux.Studentrack.persistence.entities.Faculty;

@Repository
public interface FacultyRepository extends CrudRepository<Faculty, Long> {
}
