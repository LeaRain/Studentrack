package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IStatisticsService;
import sw.laux.Studentrack.application.services.interfaces.IUserService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;

import java.lang.Module;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class StatisticsServiceRestController {
    @Autowired
    IStatisticsService statisticsService;
    @Autowired
    IUserServiceInternal userService;

    @GetMapping(value = "/student/{mailAddress}")
    public StudentDTO getStudentByMailAddress(@PathVariable("mailAddress") String mailAddress) throws StudentrackObjectNotFoundException {
        // TODO: Method in UserService for DTO
        var user = userService.findUserByMailAddress(mailAddress);

        if (!(user instanceof Student)) {
            throw new StudentrackObjectNotFoundException(Student.class, mailAddress);
        }

        var studentDto = new StudentDTO();
        studentDto.setUserId(user.getUserId());
        studentDto.setFirstName(user.getFirstName());
        studentDto.setLastName(user.getLastName());
        studentDto.setMailAddress(user.getMailAddress());
        studentDto.setEcts(((Student) user).getEcts());

        return studentDto;
    }

    @GetMapping(value = "/student/{id}")
    public StudentDTO getStudentById(@PathVariable("id") long userId) throws StudentrackObjectNotFoundException {
        return null;
    }

    @GetMapping(value = "/student/{id}/grades")
    public Map<ModuleDTO, GradeDTO> getModuleGradesForStudent(@PathVariable("id") long userId) throws StudentrackObjectNotFoundException {
        return null;
    }

    @GetMapping(value = "/student/{id}/time")
    public Map<ModuleDTO, TimeDurationDTO> getModuleTimeDurationForStudent(@PathVariable("id") long userId) throws StudentrackObjectNotFoundException {
        return null;
    }

    @GetMapping(value = "/modules")
    public Iterable<ModuleDTO> getAllModules() {
        return null;
    }

    @GetMapping(value = "/module/{id}")
    public ModuleDTO getModule(@PathVariable("@id") long moduleId) throws StudentrackObjectNotFoundException {
        return null;
    }

    @GetMapping(value = "/module/{id}/grade")
    public GradeDTO getAverageGradeForModule(@PathVariable("@id") long moduleId) throws StudentrackObjectNotFoundException {
        return null;
    }

    @GetMapping(value = "/module/{id}/time")
    public TimeDurationDTO getAverageTimeDurationForModule(@PathVariable("@id") long moduleId) throws StudentrackObjectNotFoundException {
        return null;
    }

}
