package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import java.util.Collection;

public interface IModuleService {
    Collection<Major> getAllMajors();
    Collection<Faculty> getAllFaculties();
    Iterable<Course> getAllCoursesByLecturer(Lecturer lecturer);

}
