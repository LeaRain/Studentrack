package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.ModuleNotFoundException;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import javax.transaction.Transactional;
import java.util.Collection;

public interface IModuleService {
    Collection<Major> getAllMajors();
    Collection<Faculty> getAllFaculties();
    Iterable<Module> getAllModulesByLecturer(Lecturer lecturer);
    Module saveModule(Module module);
    @Transactional
    Module updateModule(Module module) throws ModuleNotFoundException;
    Module findModule(Module module) throws ModuleNotFoundException;
    Module findModule(long moduleId) throws ModuleNotFoundException;
    Course saveCourse(Course course) throws ModuleNotFoundException;
    Course findCourse(Course course);

}
