package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw.laux.Studentrack.application.exceptions.UserAlreadyRegisteredException;
import sw.laux.Studentrack.application.services.ModuleService;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.Objects;

@Controller
public class StartController {
    @Autowired
    private IModuleService moduleService;
    @Autowired
    private IUserServiceInternal userService;
    @Autowired
    private Logger logger;

    @GetMapping("/")
    public String index(Model model,
                        @ModelAttribute("successMessage") String successMessage,
                        Principal principal) {
        if (principal != null) {
            return "redirect:/home";
        }

        if (!Objects.equals(successMessage, "")) {
            model.addAttribute("successMessage", successMessage);
        }

        return "index";
    }

    @GetMapping("registration")
    public String registration(Model model,
                               @ModelAttribute("errorMessage") String errorMessage) {

        if (!Objects.equals(errorMessage, "")) {
            model.addAttribute("errorMessage", errorMessage);
        }

        var userShell = new UserWebShell();
        var student = new Student();
        student.setMajor(new Major());
        userShell.setStudent(student);
        userShell.setIsStudent(true);
        var lecturer = new Lecturer();
        userShell.setLecturer(lecturer);
        model.addAttribute("userShell", userShell);
        var faculties = moduleService.getAllFaculties();
        model.addAttribute("faculties", faculties);
        return "registration";
    }

    @PostMapping("/registration/check")
    public String doRegistration(Model model,
                                 @ModelAttribute("userShell") UserWebShell userShell,
                                 RedirectAttributes redirectAttributes
    ) {
        User user;
        if (userShell.getIsStudent()) {
            user = userShell.getStudent();
            // Harmless cast, check for student in if condition
            user.setFaculty(((Student) user).getMajor().getFaculty());
        }

        else {
            user = userShell.getLecturer();
        }

        user.setFirstName(userShell.getFirstName());
        user.setLastName(userShell.getLastName());
        user.setMailAddress(userShell.getMailAddress());
        user.setPassword(userShell.getPassword());

        try {
            userService.registerUser(user);
            var successMessage = "Registration for user " + user.getMailAddress() + " was successful!";
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            logger.info(successMessage);
            return "redirect:/";
        }
        catch (UserAlreadyRegisteredException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.info("Attempt to register user " + user + "failed: " + exception.getMessage());
            return "redirect:/registration";
        }

    }

    @GetMapping("login")
    public String login(Model model) {
        var user = new UserImplementation();
        model.addAttribute("user", user);
        return "login";
    }


    @GetMapping("home")
    public String home(Model model, Principal principal) {
        var user = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("login-error")
    public String loginError(Model model) {
        model.addAttribute("errorMessage", "Combination of mail address and password invalid!");
        return "login";
    }

}
