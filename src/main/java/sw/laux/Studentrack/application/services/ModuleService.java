package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.*;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.repository.*;

import java.util.*;

@Service
public class ModuleService implements IModuleService {
    @Autowired
    private ModuleRepository moduleRepo;
    @Autowired
    private ModuleResultsRepository moduleResultsRepo;
    @Autowired
    private MajorRepository majorRepo;
    @Autowired
    private FacultyRepository facultyRepo;
    @Autowired
    private IUserServiceInternal userService;
    @Autowired
    private ITimeService timeService;

    public Collection<Major> getAllMajors() {
        return (Collection<Major>) majorRepo.findAll();
    }

    public Collection<Faculty> getAllFaculties() {
        return (Collection<Faculty>) facultyRepo.findAll();
    }

    @Override
    public Module saveModule(Module module) {
        return moduleRepo.save(module);
    }

    @Override
    public Module saveNewModule(Module module) throws StudentrackObjectAlreadyExistsException {
        try {
            findModule(module);
            throw new StudentrackObjectAlreadyExistsException(module.getClass(), module);
        } catch (StudentrackObjectNotFoundException ignored) {

        }

        return saveModule(module);
    }

    @Override
    public Module updateModule(Module module) throws StudentrackObjectNotFoundException {
        findModule(module);
        return saveModule(module);
    }

    @Override
    public Module findModule(Module module) throws StudentrackObjectNotFoundException {
        return findModule(module.getModuleId());
    }

    @Override
    public Module findModule(long moduleId) throws StudentrackObjectNotFoundException {
        var moduleOptional = moduleRepo.findById(moduleId);
        if (moduleOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(Module.class, moduleId);
        }

        return moduleOptional.get();
    }

    @Override
    public Iterable<Module> getAllModules() {
        return moduleRepo.findAll();
    }

    @Override
    public Iterable<Module> getNonTakenModulesByStudent(Student student) {
        var modules = getAllModules();
        var studentModules = student.getModules();

        var resultModules = new ArrayList<Module>();

        for (Module module : modules) {
            if (!studentModules.contains(module)) {
                resultModules.add(module);
            }
        }

        return resultModules;
    }

    @Override
    public Module enrollInModule(Student student, Module module) throws StudentrackObjectAlreadyExistsException, StudentrackObjectNotFoundException{
        try {
            module = findModule(module);
        } catch (StudentrackObjectNotFoundException ignored) {
        }

        student = userService.findStudent(student);

        if (student.getModules().contains(module)) {
            throw new StudentrackObjectAlreadyExistsException(module.getClass(), module);
        }

        var studentModules = student.getModules();
        studentModules.add(module);
        userService.updateUser(student);
        return module;
    }

    @Override
    public Module withdrawFromModule(Student student, Module module) throws StudentrackObjectNotFoundException {
        module = findModule(module);
        var studentModules = student.getModules();
        var removeSuccess = studentModules.remove(module);
        if (!removeSuccess) {
            throw new StudentrackObjectNotFoundException(module.getClass(), module);
        }
        userService.updateUser(student);
        return module;
    }

    @Override
    public void deleteModule(Module module) throws StudentrackObjectNotFoundException, StudentrackObjectCannotBeDeletedException {
        module = findModule(module);
        if (!module.getStudents().isEmpty()) {
            throw new StudentrackObjectCannotBeDeletedException(module.getClass(), module);
        }
        moduleRepo.delete(module);
    }

    @Override
    public Iterable<Module> getAllModulesByLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException {
        var modules = moduleRepo.findByResponsibleLecturer(lecturer);
        if (modules.isEmpty()) {
            throw new StudentrackObjectNotFoundException(Iterable.class, lecturer);
        }
        return modules.get();
    }

    @Override
    public Iterable<ModuleResults> collectResultsForAllModulesOfStudent(Student student) throws StudentrackObjectNotFoundException {
        for (var module : student.getModules()) {
            // Write all in repo
            collectResultForModuleOfStudent(student, module);
        }
        // Load all from repo
        var moduleResultsOptional = moduleResultsRepo.findAllByStudent(student);

        if (moduleResultsOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(ModuleResults.class, student);
        }

        return moduleResultsOptional.get();
    }

    @Override
    public ModuleResults collectResultForModuleOfStudent(Student student, Module module) throws StudentrackObjectNotFoundException {
        ModuleResults moduleResults;
        try {
            moduleResults = findModuleResultsForStudentAndModule(module, student);
        }
        catch (StudentrackObjectNotFoundException e) {
            moduleResults = new ModuleResults();
            moduleResults.setTimeInvest(new TimeInvest());
        }

        var timeOrders = timeService.findTimeOrdersForModuleAndStudent(module, student);

        var timeDuration = timeService.calculateTimeDuration(timeOrders);
        var timeInvest = new TimeInvest();
        timeInvest.setTimeDuration(timeDuration);
        moduleResults.setTimeInvest(timeInvest);
        timeInvest.setModuleResults(moduleResults);
        moduleResults.setStudent(student);
        student.addModuleResults(moduleResults);
        moduleResults.setModule(module);
        module.addModuleResults(moduleResults);
        moduleResultsRepo.save(moduleResults);

        return moduleResults;
    }

    @Override
    public Map<Module, TimeDuration> getTimeDurationForModulesToday(Iterable<Module> modules) {
        var moduleTimeMap = new HashMap<Module, TimeDuration>();
        
        for (var module : modules) {
            try {
                var timeDuration = getTimeDurationForModuleToday(module);
                moduleTimeMap.put(module, timeDuration);
            } catch (StudentrackObjectNotFoundException ignored) {
            }
        }

        return moduleTimeMap;
    }

    @Override
    public Map<Module, TimeDuration> getTimeDurationForLecturerModulesToday(Lecturer lecturer) {
        return getTimeDurationForModulesToday(lecturer.getModules());
    }

    @Override
    public TimeDuration getTimeDurationForModuleToday(Module module) throws StudentrackObjectNotFoundException {
        return timeService.getTimeInvestDurationForTodayAndModule(module);
    }

    @Override
    public Map<Module, TimeDuration> getTimeDurationForModules(Iterable<Module> modules) {
        var moduleTimeMap = new HashMap<Module, TimeDuration>();

        for (var module : modules) {
            try {
                var timeDuration = getTimeDurationForModule(module);
                moduleTimeMap.put(module, timeDuration);
            } catch (StudentrackObjectNotFoundException ignored) {
            }
        }

        return moduleTimeMap;
    }

    @Override
    public Map<Module, TimeDuration> getTimeDurationForLecturerModules(Lecturer lecturer) {
        return getTimeDurationForModules(lecturer.getModules());
    }

    @Override
    public TimeDuration getTimeDurationForModule(Module module) throws StudentrackObjectNotFoundException {
        return timeService.getTotalTimeInvestDurationForModule(module);
    }

    @Override
    public Iterable<ModuleResults> getResultsForModule(Module module) throws StudentrackObjectNotFoundException {
        module = findModule(module);
        var resultModuleResults = new ArrayList<ModuleResults>();
        var students = module.getStudents();

        for (var student : students) {
            var moduleResults = student.getModuleResults();
            for (var result : moduleResults) {
                if (result.getModule() == module) {
                    resultModuleResults.add(result);
                }
            }
        }

        return resultModuleResults;
    }

    @Override
    public ModuleResults saveNewGradeValueAndTryNumberForResult(Grade grade, ModuleResults moduleResults) throws StudentrackObjectNotFoundException {
        moduleResults = findModuleResults(moduleResults);
        var currentGrade = moduleResults.getGrade();

        if (currentGrade == null) {
            currentGrade = grade;
        }

        else {
            currentGrade.setValue(grade.getValue());
            currentGrade.setTryNumber(grade.getTryNumber());
        }

        currentGrade.setModuleResults(moduleResults);
        moduleResults.setGrade(currentGrade);

        moduleResults = moduleResultsRepo.save(moduleResults);

        // Clean up: If there is an open time order during successful grading, cancel it
        if (grade.getValue() != 5) {
            try {
                var openTimeOrder = timeService.findOpenTimeOrderForStudent(moduleResults.getStudent());
                timeService.deleteTimeOrder(openTimeOrder);
            } catch (StudentrackObjectNotFoundException ignored) {
            }
        }

        return moduleResults;
    }

    @Override
    public ModuleResults findModuleResults(ModuleResults moduleResults) throws StudentrackObjectNotFoundException {
        return findModuleResults(moduleResults.getModuleResultsId());
    }

    @Override
    public ModuleResults findModuleResults(long moduleResultsId) throws StudentrackObjectNotFoundException {
        var resultsOptional = moduleResultsRepo.findById(moduleResultsId);

        if (resultsOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(ModuleResults.class, moduleResultsId);
        }

        return resultsOptional.get();
    }

    @Override
    public ModuleResults findModuleResultsForStudentAndModule(Module module, Student student) throws StudentrackObjectNotFoundException {
        var resultsOptional = moduleResultsRepo.findByStudentAndModule(student, module);

        if (resultsOptional.isEmpty()) {
            throw new StudentrackObjectNotFoundException(ModuleResults.class, module);
        }

        return resultsOptional.get();
    }


}
