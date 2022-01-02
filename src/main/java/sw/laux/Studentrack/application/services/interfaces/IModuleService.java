package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.*;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public interface IModuleService {
    Collection<Major> getAllMajors();
    Collection<Faculty> getAllFaculties();
    Iterable<Module> getAllModulesByLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException;
    Module saveModule(Module module);
    Module saveNewModule(Module module) throws StudentrackObjectAlreadyExistsException;
    @Transactional
    Module updateModule(Module module) throws StudentrackObjectNotFoundException;
    Module findModule(Module module) throws StudentrackObjectNotFoundException;
    Module findModule(long moduleId) throws StudentrackObjectNotFoundException;
    Iterable<Module> getAllModules();
    Iterable<Module> getNonTakenModulesByStudent(Student student);
    @Transactional
    Module enrollInModule(Student student, Module module) throws StudentrackObjectAlreadyExistsException, StudentrackObjectNotFoundException;
    @Transactional
    Module withdrawFromModule(Student student, Module module) throws StudentrackObjectNotFoundException;
    @Transactional
    void deleteModule(Module module) throws StudentrackObjectNotFoundException, StudentrackObjectCannotBeDeletedException;
    @Transactional
    Iterable<ModuleResults> collectResultsForAllModulesOfStudent(Student student) throws StudentrackObjectNotFoundException;
    @Transactional
    ModuleResults collectResultForModuleOfStudent(Student student, Module module) throws StudentrackObjectNotFoundException;
    Map<Module, TimeDuration> getTimeDurationForModulesToday(Iterable<Module> modules);
    Map<Module, TimeDuration> getTimeDurationForLecturerModulesToday(Lecturer lecturer);
    TimeDuration getTimeDurationForModuleToday(Module module) throws StudentrackObjectNotFoundException;
    Map<Module, TimeDuration> getTimeDurationForModules(Iterable<Module> modules);
    Map<Module, TimeDuration> getTimeDurationForLecturerModules(Lecturer lecturer);
    TimeDuration getTimeDurationForModule(Module module) throws StudentrackObjectNotFoundException;
}
