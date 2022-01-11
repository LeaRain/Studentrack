package sw.laux.Studentrack.presentation.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.exceptions.StudentrackOperationNotAllowedException;
import sw.laux.Studentrack.application.exceptions.StudentrackAuthenticationException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IStatisticsService;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.application.services.interfaces.IUserService;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.presentation.WebShell.PasswordWebShell;

import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    private IModuleService moduleService;
    @Autowired
    private IUserService userService;
    @Autowired
    private Logger logger;
    @Autowired
    private ITimeService timeService;
    @Autowired
    private IStatisticsService statisticsService;

    @GetMapping("home")
    public String home(Model model, Principal principal) {
        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Student student) {
            model.addAttribute("modules", (moduleService.findCurrentlyNotPassedModulesForStudent(student)));
            model.addAttribute("moduleShell", new Module());
            try {
                var timeOrder = timeService.findOpenTimeOrderForStudent((Student) user);
                model.addAttribute("timeOrder", timeOrder);
            } catch (StudentrackObjectNotFoundException ignored) {
            }

            try {
                var moduleResults = moduleService.collectResultsForAllModulesOfStudent(student);
                model.addAttribute("moduleResults", moduleResults);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            try {
                var timeDurationToday = statisticsService.getTimeInvestDurationForTodayAndStudent(student);
                model.addAttribute("timeDurationToday", timeDurationToday);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            try {
                var timeDurationWeek = statisticsService.getTimeInvestDurationForCurrentWeekAndStudent(student);
                model.addAttribute("timeDurationWeek", timeDurationWeek);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }
        }

        if (user instanceof Lecturer lecturer) {
            var modulesWithTimeDuration = statisticsService.getTimeDurationForLecturerModules(lecturer);
            model.addAttribute("modulesWithTimeDuration", modulesWithTimeDuration);

            try {
                var timeDurationToday = statisticsService.getTotalTimeInvestDurationForToday();
                model.addAttribute("timeDurationToday", timeDurationToday);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            try {
                var timeDurationWeek = statisticsService.getTotalTimeInvestDurationForCurrentWeek();
                model.addAttribute("timeDurationWeek", timeDurationWeek);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }
        }

        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("change")
    public String changeUserData(Model model, Principal principal) {
        var user = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "change";
    }

    @PostMapping("change/check")
    public String doChange(Model model,
                                 @ModelAttribute("user") UserImplementation user,
                                 RedirectAttributes redirectAttributes) {

        try {
            userService.updateUserWithNamesAndMailAddress(user);
        } catch (StudentrackObjectNotFoundException | StudentrackObjectAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.info(e.getMessage());
            return "redirect:/home";
        }

        var successMessage = "Successfully updated " + user;
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        logger.info(successMessage);

        return "redirect:/home";
    }

    @GetMapping("change/password")
    public String changePassword(Model model) {
        var passwordShell = new PasswordWebShell();
        model.addAttribute("passwordShell", passwordShell);
        return "passwordchange";
    }

    @PostMapping("change/password/check")
    public String doChangePassword(Model model,
                           Principal principal,
                           @ModelAttribute("passwordShell") PasswordWebShell passwordWebShell,
                           RedirectAttributes redirectAttributes) {
        var user = (User) userService.loadUserByUsername(principal.getName());
        try {
            userService.changeUserPassword(user, passwordWebShell.getOldPassword(), passwordWebShell.getNewPassword());
        } catch (StudentrackObjectNotFoundException | StudentrackAuthenticationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.info(e.getMessage());
            return "redirect:/home";
        }

        var successMessage = "Successfully updated password for user " + user;
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        logger.info(successMessage);

        return "redirect:/home";

    }

    @PostMapping("change/delete")
    public String doDeleteAccount(Model model,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {

        var user = (User) userService.loadUserByUsername(principal.getName());
        try {
            userService.deleteUser(user);
        } catch (StudentrackObjectNotFoundException | StudentrackOperationNotAllowedException e) {
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage");
            return "redirect:/home";
        }

        var successMessage = "Successfully deleted user " + user;
        logger.info(successMessage);
        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return "redirect:/logout";
    }
}
