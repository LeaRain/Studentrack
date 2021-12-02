package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import sw.laux.Studentrack.persistence.repository.IGradeRepo;
import sw.laux.Studentrack.persistence.repository.IModuleRepo;

public class ModuleService {
    @Autowired
    private IModuleRepo moduleRepo;
    @Autowired
    private IGradeRepo gradeRepo;
}
