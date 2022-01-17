package sw.laux.Studentrack.presentation.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw.helblingd.terminportalbackend.repository.entity.AppointmentGroup;
import sw.laux.Studentrack.application.exceptions.*;
import sw.laux.Studentrack.application.services.interfaces.IAppointmentService;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IStatisticsService;
import sw.laux.Studentrack.application.services.interfaces.IUserService;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.presentation.WebShell.ModuleResultsListShell;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.presentation.WebShell.ModuleShell;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

@Controller
public class ModuleController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private Logger logger;

    @GetMapping("modules")
    public String modules(Model model, Principal principal,
                          @ModelAttribute("successMessage") String successMessage,
                          @ModelAttribute("errorMessage") String errorMessage) {

        model.addAttribute("successMessage", successMessage);
        model.addAttribute("errorMessage", errorMessage);

        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Student) {
            var student = (Student) user;
            // Also failed modules for withdrawing -> 5.0 makes withdraw possible, for example in case of choosing another module for an exam
            model.addAttribute("modules", moduleService.findCurrentlyNotPassedModulesForStudent(student));
            try {
                var moduleResults = moduleService.collectResultsForAllModulesOfStudent(student);
                model.addAttribute("moduleResults", moduleResults);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }
            try {
                student = userService.calculateCurrentECTSOfStudent(student);
                // User is loaded out of user service -> will exist
            } catch (StudentrackObjectNotFoundException ignored) {

            }
            model.addAttribute("student", student);
            model.addAttribute("durationStatistics", statisticsService.getCurrentProgressOfStudentInAllModules(student));
        }

        if (user instanceof Lecturer) {
            var lecturer = (Lecturer) user;
            Iterable<Module> modules = null;
            try {
                // Harmless case, because instance is checked.
                modules = moduleService.getAllModulesByLecturer(lecturer);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }
            model.addAttribute("modules", modules);
        }

        model.addAttribute("faculty", user.getFaculty());
        var moduleShell = new Module();
        model.addAttribute("moduleShell", moduleShell);

        return "modules";
    }

    @GetMapping("modules/new")
    public String newModule(Model model,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {

        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Student) {
            var student = (Student) user;
            var modules = moduleService.getNonTakenAndAvailableModulesByStudent(student);

            if (!(modules.iterator().hasNext())) {
                var errorMessage = "New modules are currently not available";
                logger.info(errorMessage);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/modules";
            }

            model.addAttribute("modules", modules);
            model.addAttribute("faculty", user.getFaculty());
        }

        var moduleShell = new ModuleShell();
        model.addAttribute("moduleShell", moduleShell);

        return "newmodule";
    }

    @PostMapping("modules/new/check")
    public String doNewModule(Model model,
                              Principal principal,
                              @ModelAttribute("moduleShell") ModuleShell moduleShell,
                              RedirectAttributes redirectAttributes) {
        var module = moduleShell.getModule();

        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Lecturer) {
            var lecturer = (Lecturer) user;
            module.setResponsibleLecturer(lecturer);
            module.setStartDate(frontendDateStringToDate(moduleShell.getStartString()));
            try {
                moduleService.saveNewModule(module);
                var successMessage = "Successfully saved module " + module;
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
                logger.info(successMessage);
            }

            catch (StudentrackObjectAlreadyExistsException exception) {
                var errorMessage = "New module cannot be created, because a module with name " + module.getName() + " already exists: " + exception.getMessage();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                logger.info(errorMessage);
            }
        }

        if (user instanceof Student) {
            var student = (Student) user;
            try {
                module = moduleService.enrollInModule(student, module);
                var successMessage = "Successfully enrolled " + student + " in module " + module;
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
                logger.info(successMessage);

            }
            catch (StudentrackObjectAlreadyExistsException e) {
                var errorMessage = "You are already enrolled in " + module.getName() + ": " + e.getMessage();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                logger.info(errorMessage);
            }

            catch (StudentrackObjectNotFoundException e) {
                var errorMessage = "Student or external schedule cannot be found: " + e.getMessage();
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                logger.info(errorMessage);
            }
        }

        return "redirect:/modules";
    }

    @PostMapping("modules/edit")
    public String doEditModule(Model model,
                               @ModelAttribute("moduleShell") Module module,
                               RedirectAttributes redirectAttributes) {

        // Existence check for module in redirected page -> Here not necessary, even if moduleId is not set (and therefore 0)
        return "redirect:/modules/edit/" + module.getModuleId();
    }

    @GetMapping("modules/edit/{moduleId}")
    public String doEditModule(Model model,
                               @PathVariable long moduleId,
                               RedirectAttributes redirectAttributes) {
        Module module;

        try {
            module = moduleService.findModule(moduleId);
        }
        catch (StudentrackObjectNotFoundException e) {
            var errorMessage = "Module with id " + moduleId + " cannot be found: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);
            return "redirect:/modules";
        }

        model.addAttribute("module", module);

        return "editmodule";
    }

    @PostMapping("modules/edit/check")
    public String doEditModuleCheck(Model model,
                                    @ModelAttribute("module") Module module,
                                    RedirectAttributes redirectAttributes) {

        try {
            moduleService.updateModule(module);

        } catch (StudentrackObjectNotFoundException e) {
            var errorMessage = "Module " + module + "cannot be found: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);
            return "redirect:/modules";
        }

        var successMessage = "Module " + module + " successfully updated!";
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        logger.info(successMessage);

        return "redirect:/modules";
    }

    @PostMapping("modules/withdraw")
    public String doWithdrawModule(Model model,
                               Principal principal,
                               @ModelAttribute("moduleShell") Module module,
                               RedirectAttributes redirectAttributes) {

        var user = (User) userService.loadUserByUsername(principal.getName());

        try {
            // Cast possible due to check for authorities with Spring Security and access only for Students
            module = moduleService.withdrawFromModule((Student) user, module);
            var successMessage = "Successfully withdrawn from module " + module;
            logger.info(successMessage);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        }

        catch (StudentrackObjectNotFoundException e) {
            var errorMessage = "Module " + module + " not found, student cannot be withdrawn: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);
        }

        catch(StudentrackOperationNotAllowedException e) {
            var errorMessage = "Withdraw for module " + module + " not possible, because withdraw for passed modules not possible: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);
        }

        return "redirect:/modules";
    }

    @PostMapping("modules/delete")
    public String doDeleteModule(Model model,
                                 @ModelAttribute("module") Module module,
                                 RedirectAttributes redirectAttributes) {

        try {
            moduleService.deleteModule(module);
            var successMessage = "Successfully deleted module " + module;
            logger.info(successMessage);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        }
        catch (StudentrackOperationNotAllowedException exception) {
            var errorMessage = "Module " + module + " cannot be deleted, because students are currently enrolled: " + exception.getMessage();
            logger.info(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        catch(StudentrackObjectNotFoundException exception) {
            var errorMessage = "Module " + module.getName() + " cannot be found and therefore not be deleted: " + exception.getMessage();
            logger.info(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        return "redirect:/modules";
    }

    @PostMapping("modules/grade")
    public String doEnterGrades(Model model,
                                    @ModelAttribute("module") Module module,
                                    RedirectAttributes redirectAttributes) {

        return "redirect:/modules/grade/" + module.getModuleId();

    }

    @GetMapping("modules/grade/{moduleId}")
    public String doEnterGrades(Model model,
                               @PathVariable long moduleId,
                               RedirectAttributes redirectAttributes) {

        Module module;

        try {
            module = moduleService.findModule(moduleId);
        }
        catch (StudentrackObjectNotFoundException e) {
            var errorMessage = "Module with id " + moduleId + " cannot be found: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);
            return "redirect:/modules";
        }

        model.addAttribute("module", module);

        Iterable<ModuleResults> moduleResults;

        try {
            moduleResults = moduleService.getResultsForModule(module);
        } catch (StudentrackObjectNotFoundException e) {
            var errorMessage = "Module with id " + moduleId + " cannot be found: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);
            return "redirect:/modules";
        }

        var moduleResultsShell = new ModuleResultsListShell();

        for (var result : moduleResults) {
            if (result.getGrade() == null) {
                result.setGrade(new Grade());
            }

            moduleResultsShell.addModuleResult(result);
        }

        model.addAttribute("moduleResults", moduleResults);
        model.addAttribute("moduleResultsShell", moduleResultsShell);

        return "grades";
    }

    @PostMapping("modules/grade/check")
    public String doGradeCheck(Model model,
                                    @ModelAttribute("moduleResultsShell") ModuleResultsListShell moduleResultsListShell,
                                    RedirectAttributes redirectAttributes) {

        var moduleResults = moduleResultsListShell.getModuleResults();
        var correctGradeValues = new Double[]{1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0, 5.0};
        var successMessage = new StringBuilder();
        var errorMessage = new StringBuilder();

        for (var result : moduleResults) {
            var grade = result.getGrade();
            if (!(Arrays.asList(correctGradeValues).contains(grade.getValue()))) {
                var gradeValueErrorMessage = "Grade value " + grade.getValue() + " not allowed";
                redirectAttributes.addFlashAttribute("errorMessage", gradeValueErrorMessage);
                logger.info(gradeValueErrorMessage);
                return "redirect:/modules";
            }

            if (grade.getTryNumber() < 1 || grade.getTryNumber() > 3) {
                var gradeTryNumberErrorMessage = "Try number " + grade.getTryNumber() + " not allowed";
                redirectAttributes.addFlashAttribute("errorMessage", gradeTryNumberErrorMessage);
                logger.info(gradeTryNumberErrorMessage);
                return "redirect:/modules";
            }

            try {
                var newResult = moduleService.saveNewGradeValueAndTryNumberForResult(grade, result);
                var gradeSuccessMessage = "Successfully saved grade " + newResult.getGrade() + " for " + newResult.getStudent();
                logger.info(gradeSuccessMessage);
                successMessage.append("\n").append(gradeSuccessMessage);

            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
                errorMessage.append("\n").append(e.getMessage());
            }

            if (!(successMessage.isEmpty())) {
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
            }

            if (!(errorMessage.isEmpty())) {
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            }

        }

        return "redirect:/modules";
    }

    // Inspiration by https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    public Date frontendDateStringToDate(String dateString) {
        return Date.from(
                LocalDateTime.parse(dateString)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );
    }
}