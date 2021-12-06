package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.persistence.entities.Faculty;
import sw.laux.Studentrack.persistence.entities.Major;
import sw.laux.Studentrack.persistence.repository.FacultyRepository;
import sw.laux.Studentrack.persistence.repository.GradeRepository;
import sw.laux.Studentrack.persistence.repository.MajorRepository;
import sw.laux.Studentrack.persistence.repository.ModuleRepository;

import java.util.Collection;

@Service
public class ModuleService {
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

}
