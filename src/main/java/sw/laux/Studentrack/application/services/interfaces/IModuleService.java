package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.ModuleAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.ModuleCannotBeDeletedException;
import sw.laux.Studentrack.application.exceptions.ModuleNotFoundException;
import sw.laux.Studentrack.application.exceptions.UserNotFoundException;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import javax.transaction.Transactional;
import java.util.Collection;

public interface IModuleService {
    Collection<Major> getAllMajors();
    Collection<Faculty> getAllFaculties();
    Iterable<Module> getAllModulesByLecturer(Lecturer lecturer) throws ModuleNotFoundException;
    Module saveModule(Module module);
    Module saveNewModule(Module module) throws ModuleAlreadyExistsException;
    @Transactional
    Module updateModule(Module module) throws ModuleNotFoundException;
    Module findModule(Module module) throws ModuleNotFoundException;
    Module findModule(long moduleId) throws ModuleNotFoundException;
    Iterable<Module> getAllModules();
    Iterable<Module> getNonTakenModulesByStudent(Student student);
    @Transactional
    Module enrollInModule(Student student, Module module) throws ModuleAlreadyExistsException, UserNotFoundException;
    @Transactional
    Module withdrawFromModule(Student student, Module module) throws ModuleNotFoundException;
    @Transactional
    void deleteModule(Module module) throws ModuleNotFoundException, ModuleCannotBeDeletedException;

}
