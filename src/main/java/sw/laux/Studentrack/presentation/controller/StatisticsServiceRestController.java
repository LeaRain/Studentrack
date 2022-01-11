package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sw.laux.Studentrack.application.DTO.*;
import sw.laux.Studentrack.application.exceptions.StudentrackAuthenticationException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IStatisticsService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;

import java.util.Map;

/*
A little word about the REST API infrastructure:
The authentication is handled by a custom API interface, saving a key with expiration date in a hashed format in the database.
The key itself is transmitted in plain text as answer (and in the frontend), which can be interpreted as security flaw.
In addition, literally anyone can get an API key by entering a mail address and organization.
In a more realistic situation, there would be an automated process for validating and authenticating the user.
There would be for example a list of organizations, which are allowed to access the API with their custom mail addresses.
So there would be a validation for them or even the possibility to register and manually get access by an employer of Studentrack.
Please regard also the fact, that the API is (nearly) read only, so it is not possible to change data randomly by anyone.
Only the registration for a token is not read only, but the risk for private data here is very low.
Since Studentrack is a simple software project for educational purposes, the limitations described here are to consider for productive usage.
This basic authentication of an API key should only show an example and a first draft.
The API also uses mainly DTOs.
This keeps the complexity on a low level, so the user only needs the DTOs with rather primitive data types instead of all entities with their relations to each other.
 */

@RequestMapping("/api")
@RestController
public class StatisticsServiceRestController {
    @Autowired
    IStatisticsService statisticsService;
    @Autowired
    IUserServiceInternal userService;

    @PostMapping(value = "/register")
    public APIKeyDTO registerNewDeveloper(@RequestBody DeveloperDTO developerDTO) throws StudentrackObjectAlreadyExistsException {
        var developer = new Developer();
        developer.setMailAddress(developerDTO.getMailAddress());
        developer.setOrganization(developerDTO.getOrganization());
        developer = userService.preregisterDeveloper(developer);
        var apiKeyDTO = new APIKeyDTO();
        apiKeyDTO.setKey(developer.getKey().getKey());
        apiKeyDTO.setMailAddress(developer.getMailAddress());
        userService.commitRegisterDeveloper(developer);
        return apiKeyDTO;
    }

    @GetMapping(value = "/student/mail/{mailAddress}")
    public StudentDTO getStudentByMailAddress(@RequestBody APIKeyDTO apiKey,
                                              @PathVariable(value="mailAddress") String mailAddress) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException {
        validateKey(apiKey);
        return userService.findStudentDTOByMailAddress(mailAddress);
    }

    @GetMapping(value = "/student/{id}")
    public StudentDTO getStudentById(@RequestBody APIKeyDTO apiKey,
                                     @PathVariable("id") long userId) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException {
        validateKey(apiKey);
        return userService.findStudentDTOByUserId(userId);
    }

    @GetMapping(value = "/student/{id}/grades")
    public Map<ModuleDTO, GradeDTO> getModuleGradesForStudent(@RequestBody APIKeyDTO apiKey,
                                                              @PathVariable("id") long userId) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException {
        validateKey(apiKey);
        var student = userService.findStudent(userId);
        return statisticsService.getModuleGradesForStudent(student);
    }

    @GetMapping(value = "/student/{id}/time")
    public Map<ModuleDTO, TimeDurationDTO> getModuleTimeDurationForStudent(@RequestBody APIKeyDTO apiKey,
                                                                           @PathVariable("id") long userId) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException {
        validateKey(apiKey);
        var student = userService.findStudent(userId);
        return statisticsService.getModuleTimeDurationForStudent(student);
    }

    @GetMapping(value = "/module")
    public Iterable<ModuleDTO> getAllModules(@RequestBody APIKeyDTO apiKey) throws StudentrackAuthenticationException, StudentrackObjectNotFoundException {
        validateKey(apiKey);
        return statisticsService.getAllModules();
    }

    @GetMapping(value = "/module/{id}")
    public ModuleDTO getModule(@RequestBody APIKeyDTO apiKey,
                               @PathVariable("id") long moduleId) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException {
        validateKey(apiKey);
        return statisticsService.getModuleDTOBasedOnModuleId(moduleId);
    }

    @GetMapping(value = "/module/{id}/grade")
    public GradeDTO getAverageGradeForModule(@RequestBody APIKeyDTO apiKey,
                                             @PathVariable("id") long moduleId) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException {
        validateKey(apiKey);
        return statisticsService.getAverageGradeDTOForModule(moduleId);
    }

    @GetMapping(value = "/module/{id}/time")
    public TimeDurationDTO getAverageTimeDurationForModule(@RequestBody APIKeyDTO apiKey,
                                                           @PathVariable("id") long moduleId) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException {
        validateKey(apiKey);
        return statisticsService.getAverageTimeDurationDTOForModule(moduleId);
    }

    public void validateKey(APIKeyDTO apiKey) throws StudentrackObjectNotFoundException, StudentrackAuthenticationException {
        userService.validateAPIKey(apiKey);
    }

}
