package sw.laux.Studentrack.presentation.controller;

import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw.laux.Studentrack.application.exceptions.UserAlreadyRegisteredException;
import sw.laux.Studentrack.application.services.ModuleService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

@Controller
public class StartController {
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private IUserServiceInternal userService;

    private final Logger logger = LogManager.getLogger(this.getClass());

    @GetMapping("/")
    public String index(Model model,
                        @ModelAttribute("successMessage") String successMessage) {
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
            redirectAttributes.addFlashAttribute("successMessage", "Registration for user " + user.getMailAddress() + " was successful!");
            return "redirect:/";
        }
        catch (UserAlreadyRegisteredException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/registration";
        }

    }

    @GetMapping("login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("login")
    public String doLogin() {
        return "home";

    }

    @GetMapping("home")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("login-error")
    public String loginError(Model model) {
        model.addAttribute("errorMessage", "Combination of mail address and password invalid!");
        return "login";
    }

}
