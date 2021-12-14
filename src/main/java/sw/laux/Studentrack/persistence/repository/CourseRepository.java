package sw.laux.Studentrack.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sw.laux.Studentrack.persistence.entities.Course;
import sw.laux.Studentrack.persistence.entities.Lecturer;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{
    @Query("SELECT c FROM Course AS c WHERE c.module.responsibleLecturer = :lecturer")
    Iterable<Course> findByModulesResponsibleLecturer(@Param("lecturer") Lecturer lecturer);
}
