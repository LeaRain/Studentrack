package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.persistence.repository.GradeRepository;
import sw.laux.Studentrack.persistence.repository.ModuleRepository;

@Service
public class ModuleService {
    @Autowired
    private ModuleRepository moduleRepo;
    @Autowired
    private GradeRepository gradeRepo;
}
