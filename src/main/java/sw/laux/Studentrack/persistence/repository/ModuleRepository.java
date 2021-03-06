package sw.laux.Studentrack.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sw.laux.Studentrack.persistence.entities.Lecturer;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.Student;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ModuleRepository extends CrudRepository<Module, Long> {
    Optional<Iterable<Module>> findByResponsibleLecturer(Lecturer responsibleLecturer);
    Optional<Module> findModuleByName(String name);
    Iterable<Module> findAllByResponsibleLecturerNotNull();
}
