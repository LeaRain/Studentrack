package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.ModuleAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.ModuleNotFoundException;
import sw.laux.Studentrack.application.exceptions.UserNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.repository.*;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ModuleService implements IModuleService {
    @Autowired
    private ModuleRepository moduleRepo;
    @Autowired
    private GradeRepository gradeRepo;
    @Autowired
    private MajorRepository majorRepo;
    @Autowired
    private FacultyRepository facultyRepo;
    @Autowired
    private IUserServiceInternal userService;

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
    public Module saveNewModule(Module module) throws ModuleAlreadyExistsException {
        return null;
    }

    @Override
    public Module updateModule(Module module) throws ModuleNotFoundException {
        findModule(module);
        return saveModule(module);
    }

    @Override
    public Module findModule(Module module) throws ModuleNotFoundException {
        return findModule(module.getModuleId());
    }

    @Override
    public Module findModule(long moduleId) throws ModuleNotFoundException {
        var moduleOptional = moduleRepo.findById(moduleId);
        if (moduleOptional.isEmpty()) {
            throw new ModuleNotFoundException("Module with id " + moduleId + " not found!");
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
    public Module enrollInModule(Student student, Module module) throws ModuleAlreadyExistsException, UserNotFoundException {
        try {
            module = findModule(module);
        } catch (ModuleNotFoundException ignored) {
        }

        student = userService.findStudent(student);

        if (student.getModules().contains(module)) {
            throw new ModuleAlreadyExistsException("Student " + student + " is already enrolled in Module " + module);
        }

        var studentModules = student.getModules();
        studentModules.add(module);
        module.addStudent(student);
        moduleRepo.save(module);
        userService.updateUser(student);
        return module;
    }

    @Override
    public Module withdrawFromModule(Student student, Module module) throws ModuleNotFoundException {
        module = findModule(module);
        var studentModules = student.getModules();
        var removeSuccess = studentModules.remove(module);
        if (!removeSuccess) {
            throw new ModuleNotFoundException("Student cannot be withdrawn from module " + module + ", because the module cannot be found.");
        }
        module.removeStudent(student);
        userService.updateUser(student);
        return module;
    }

    @Override
    public Iterable<Module> getAllModulesByLecturer(Lecturer lecturer) throws ModuleNotFoundException {
        var modules = moduleRepo.findByResponsibleLecturer(lecturer);
        if (modules.isEmpty()) {
            throw new ModuleNotFoundException("Modules for lecturer " + lecturer + "not found!");
        }
        return modules.get();
    }


}
