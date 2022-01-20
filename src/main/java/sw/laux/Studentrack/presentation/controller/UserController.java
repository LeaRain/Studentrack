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
import sw.laux.Studentrack.application.services.interfaces.*;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.presentation.WebShell.PasswordWebShell;

import java.io.IOException;
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
    @Autowired
    private IAppointmentService appointmentService;

    @GetMapping("home")
    public String home(Model model, Principal principal) {
        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Student) {
            var student = (Student) user;
            model.addAttribute("modules", (moduleService.findCurrentlyNotPassedModulesForStudent(student)));
            model.addAttribute("moduleShell", new Module());
            try {
                var timeOrder = timeService.findOpenTimeOrderForStudent(student);
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

        if (user instanceof Lecturer) {
            var lecturer = (Lecturer) user;
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
        if (user instanceof Lecturer || (user instanceof Student && ((Student) user).isPremiumUser())) {
            model.addAttribute("isPremium", true);
        }

        else {
            model.addAttribute("isPremium", false);
        }

        if (user instanceof Lecturer) {
            var lecturer = (Lecturer) user;
            model.addAttribute("isLecturer", true);
            var key = lecturer.getAppointmentServiceApiKey();

            if (key == null) {
                model.addAttribute("isAuthenticated", false);
            }

            else {
                model.addAttribute("isAuthenticated", true);
            }
        }

        else {
            model.addAttribute("isLecturer", false);
        }

        var bankingAuthenticated = user.getBankingServiceApiKey() != null;
        model.addAttribute("bankingAuthenticated", bankingAuthenticated);
        model.addAttribute("user", user);
        return "change";
    }

    @PostMapping("change/check")
    public String doChange(Model model,
                                 @ModelAttribute("user") UserImplementation user,
                                 RedirectAttributes redirectAttributes) {

        try {
            userService.updateUserWithNamesAndMailAddress(user);
            var successMessage = "Successfully updated " + user;
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            logger.info(successMessage);
        }
        catch (StudentrackObjectNotFoundException e) {
            var errorMessage = "User " + user + " cannot be found: " + e.getMessage();
            logger.info(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }
        catch (StudentrackObjectAlreadyExistsException e) {
            var errorMessage = "User with mail address " + user.getMailAddress() + " already exists: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(e.getMessage());
        }

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
            var successMessage = "Successfully updated password for user " + user;
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            logger.info(successMessage);
        }
        catch (StudentrackObjectNotFoundException e) {
            var errorMessage = "User " + user + " cannot be found: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);
        }
        catch( StudentrackAuthenticationException e) {
            var errorMessage = "Old password is wrong, cannot change password: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);
            return "redirect:/home";
        }

        return "redirect:/home";

    }

    @PostMapping("change/delete")
    public String doDeleteAccount(Model model,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {

        var user = (User) userService.loadUserByUsername(principal.getName());
        try {
            userService.deleteUser(user);
        }
        catch (StudentrackObjectNotFoundException e) {
            var errorMessage = "User " + user + " not found: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);
            return "redirect:/home";
        }

        var successMessage = "Successfully deleted user " + user;
        logger.info(successMessage);
        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return "redirect:/logout";
    }

    @GetMapping("change/premium")
    public String premium(Model model,
                          Principal principal,
                          RedirectAttributes redirectAttributes) {
        var user = (User) userService.loadUserByUsername(principal.getName());

        if (!(user instanceof Student)) {
            var errorMessage = "Wrong user type for " + user;
            logger.info(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/home";
        }

        model.addAttribute("isPremium", ((Student) user).isPremiumUser());
        return "premium";
    }

    @PostMapping("change/premium/check")
    public String doPremium(Model model,
                          Principal principal,
                          RedirectAttributes redirectAttributes) {
        var user = (User) userService.loadUserByUsername(principal.getName());

        try {
            userService.upgradeStudentToPremium((Student) user);
            var successMessage = "Upgrade to premium successfully for " + user;
            logger.info(successMessage);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        }
        catch (StudentrackAuthenticationException e) {
            var errorMessage = "Authentication with the banking service failed. Please try again later: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);

        }
        catch (StudentrackObjectNotFoundException e) {
            var errorMessage = "User " + user + " not found: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);
        }

        catch (StudentrackOperationNotAllowedException e) {
            var errorMessage = "Not enough money for transaction: " + e.getMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            logger.info(errorMessage);
        }

        return "redirect:/home";
    }

    @GetMapping("change/appointment")
    public String authenticateAppointmentService(Model model,
                                                 Principal principal,
                                                 RedirectAttributes redirectAttributes) {
        var user = (User) userService.loadUserByUsername(principal.getName());

        if (!(user instanceof Lecturer)) {
            var errorMessage = "Wrong user type for " + user;
            logger.info(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/home";
        }

        if (user.getAppointmentServiceApiKey() != null) {
            user.setAppointmentServiceApiKey(null);
            user = userService.updateUser(user);
            var successMessage = "Successfully revoked authentication for appoinment service for " + user;
            logger.info(successMessage);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            return "redirect:/home";
        }

        try {
            var key = appointmentService.getAuthenticationKey();
            user.setAppointmentServiceApiKey(key);
            userService.updateUser(user);
            // TODO: deployment server path
            return "redirect:http://localhost:7000/login/api?apiKey=" + key;
        } catch (StudentrackObjectNotFoundException e) {
            var errorMessage = "Authentication for appointment service failed: " + e.getMessage();
            logger.info(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/home";
        }
    }

    @GetMapping("change/banking")
    public String authenticateBankingService(Model model,
                                                 Principal principal,
                                                 RedirectAttributes redirectAttributes) {
        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user.getBankingServiceApiKey() != null) {
            var errorMessage = "User " + user + " already authenticated at bank!";
            logger.info(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/home";
        }

        try {
            userService.registerUserAtBankingService(user);
        } catch (StudentrackOperationNotAllowedException | StudentrackObjectNotFoundException e) {
            var errorMessage = "User " + user + " could not be authenticated:" + e.getMessage();
            logger.info(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        var successMessage = "Successfully authenticated " + user;
        logger.info(successMessage);
        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return "redirect:/home";
    }
}