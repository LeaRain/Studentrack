package sw.laux.Studentrack.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sw.laux.Studentrack.persistence.entities.Lecturer;
import sw.laux.Studentrack.persistence.entities.Module;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Long> {
    Iterable<Module> findByResponsibleLecturer(Lecturer responsibleLecturer);
}
