package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IStatisticsService;
import sw.laux.Studentrack.application.services.interfaces.IUserService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;

import java.util.Map;

@RequestMapping("/api")
@RestController
public class StatisticsServiceRestController {
    @Autowired
    IStatisticsService statisticsService;
    @Autowired
    IUserServiceInternal userService;

    // TODO: API token

    @GetMapping(value = "/student/mail/{mailAddress}")
    public StudentDTO getStudentByMailAddress(@PathVariable(value="mailAddress") String mailAddress) throws StudentrackObjectNotFoundException {
        return userService.findStudentDTOByMailAddress(mailAddress);
    }

    @GetMapping(value = "/student/{id}")
    public StudentDTO getStudentById(@PathVariable("id") long userId) throws StudentrackObjectNotFoundException {
        return userService.findStudentDTOByUserId(userId);
    }

    @GetMapping(value = "/student/{id}/grades")
    public Map<ModuleDTO, GradeDTO> getModuleGradesForStudent(@PathVariable("id") long userId) throws StudentrackObjectNotFoundException {
        var student = userService.findStudent(userId);
        return statisticsService.getModuleGradesForStudent(student);
    }

    @GetMapping(value = "/student/{id}/time")
    public Map<ModuleDTO, TimeDurationDTO> getModuleTimeDurationForStudent(@PathVariable("id") long userId) throws StudentrackObjectNotFoundException {
        var student = userService.findStudent(userId);
        return statisticsService.getModuleTimeDurationForStudent(student);
    }

    @GetMapping(value = "/module")
    public Iterable<ModuleDTO> getAllModules() {
        return statisticsService.getAllModules();
    }

    @GetMapping(value = "/module/{id}")
    public ModuleDTO getModule(@PathVariable("id") long moduleId) throws StudentrackObjectNotFoundException {
        return statisticsService.getModuleDTOBasedOnModuleId(moduleId);
    }

    @GetMapping(value = "/module/{id}/grade")
    public GradeDTO getAverageGradeForModule(@PathVariable("id") long moduleId) throws StudentrackObjectNotFoundException {
        return statisticsService.getAverageGradeDTOForModule(moduleId);
    }

    @GetMapping(value = "/module/{id}/time")
    public TimeDurationDTO getAverageTimeDurationForModule(@PathVariable("id") long moduleId) throws StudentrackObjectNotFoundException {
        return statisticsService.getAverageTimeDurationDTOForModule(moduleId);
    }

}
