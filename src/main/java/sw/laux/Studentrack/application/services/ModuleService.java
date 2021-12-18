package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.ModuleNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.repository.*;

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
    public Iterable<Module> getAllModulesByLecturer(Lecturer lecturer) {
        return moduleRepo.findByResponsibleLecturer(lecturer);
    }


}
