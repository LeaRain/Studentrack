package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.*;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import javax.transaction.Transactional;

public interface IModuleService {
    Iterable<Module> getAllModulesByLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException;
    Module saveModule(Module module);
    Module saveNewModule(Module module) throws StudentrackObjectAlreadyExistsException;
    @Transactional
    Module updateModule(Module module) throws StudentrackObjectNotFoundException;
    Module findModule(Module module) throws StudentrackObjectNotFoundException;
    Module findModule(long moduleId) throws StudentrackObjectNotFoundException;
    Module findModuleByName(String name) throws StudentrackObjectNotFoundException;
    Iterable<Module> getAllAvailableModules();
    Iterable<Module> getNonTakenAndAvailableModulesByStudent(Student student);
    Iterable<Module> findAllModules();
    @Transactional
    Module enrollInModule(Student student, Module module) throws StudentrackObjectAlreadyExistsException, StudentrackObjectNotFoundException;
    @Transactional
    Module withdrawFromModule(Student student, Module module) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException;
    @Transactional
    Module withdrawAllStudentsWithoutSuccessfulGradeFromModule(Module module) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException;
    @Transactional
    void deleteModule(Module module) throws StudentrackObjectNotFoundException, StudentrackOperationNotAllowedException;
    @Transactional
    void deleteAllModulesOfLecturer(Lecturer lecturer);
    @Transactional
    void deleteModulesWithoutLecturerAndModuleResults() throws StudentrackObjectNotFoundException;
    @Transactional
    boolean hasModuleModuleResults(Module module);
    Iterable<ModuleResults> collectResultsForAllModulesOfStudent(Student student) throws StudentrackObjectNotFoundException;
    @Transactional
    ModuleResults collectResultForModuleOfStudent(Student student, Module module) throws StudentrackObjectNotFoundException;
    @Transactional
    Iterable<ModuleResults> collectResultsForModule(Module module) throws StudentrackObjectNotFoundException;
    Iterable<ModuleResults> getResultsForModule(Module module) throws StudentrackObjectNotFoundException;
    @Transactional
    ModuleResults saveNewGradeValueAndTryNumberForResult(Grade grade, ModuleResults moduleResults) throws StudentrackObjectNotFoundException;
    ModuleResults findModuleResults(ModuleResults moduleResults) throws StudentrackObjectNotFoundException;
    ModuleResults findModuleResults(long moduleResultsId) throws StudentrackObjectNotFoundException;
    ModuleResults findModuleResultsForStudentAndModule(Module module, Student student) throws StudentrackObjectNotFoundException;
    Iterable<Module> findCurrentlyNotPassedModulesForStudent(Student student);
    int calculateECTSOfStudent(Student student);
    boolean hasStudentPassedModule(Student student, Module module);
}
