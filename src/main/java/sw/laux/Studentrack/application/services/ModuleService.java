package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.*;
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


}
