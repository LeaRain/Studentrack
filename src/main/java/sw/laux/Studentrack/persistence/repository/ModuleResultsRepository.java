package sw.laux.Studentrack.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.ModuleResults;
import sw.laux.Studentrack.persistence.entities.Student;

import java.util.Optional;

public interface ModuleResultsRepository extends CrudRepository<ModuleResults, Long> {
    Optional<ModuleResults> findByStudentAndModule(Student student, Module module);
    Optional<Iterable<ModuleResults>> findAllByStudent(Student student);
}
