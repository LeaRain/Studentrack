package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.services.interfaces.IUserService;
import sw.laux.Studentrack.persistence.entities.*;
import org.slf4j.Logger;
import sw.laux.Studentrack.presentation.WebShell.UserWebShell;

import java.security.Principal;

@Controller
public class StartController {
    @Autowired
    private IUserService userService;
    @Autowired
    private Logger logger;

    @GetMapping("/")
    public String index(Model model,
                        @ModelAttribute("successMessage") String successMessage,
                        Principal principal) {
        if (principal != null) {
            return "redirect:/home";
        }

        model.addAttribute("successMessage", successMessage);

        return "index";
    }

    @GetMapping("registration")
    public String registration(Model model,
                               @ModelAttribute("errorMessage") String errorMessage) {

        model.addAttribute("errorMessage", errorMessage);

        var userShell = new UserWebShell();
        var student = new Student();
        student.setMajor(new Major());
        userShell.setStudent(student);
        userShell.setIsStudent(true);
        var lecturer = new Lecturer();
        userShell.setLecturer(lecturer);
        model.addAttribute("userShell", userShell);
        var faculties = userService.getAllFaculties();
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
            user.setFaculty(userShell.getFaculty());
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
        catch (StudentrackObjectAlreadyExistsException exception) {
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

    @GetMapping("login-error")
    public String loginError(Model model) {
        model.addAttribute("errorMessage", "Combination of mail address and password invalid!");
        return "login";
    }

    @GetMapping("impress")
    public String impress() {
        return "impress";
    }

    @GetMapping("privacy")
    public String privacy() {
        return "privacy";
    }

    @GetMapping("developer")
    public String developer(Model model) {
        var developer = new Developer();
        model.addAttribute("developer", developer);
        return "developer";
    }

    @PostMapping("developer/check")
    public String checkDeveloper(Model model,
                                 @ModelAttribute("developer") Developer developer,
                                 RedirectAttributes redirectAttributes) {

        try {
            developer = userService.preregisterDeveloper(developer);
        } catch (StudentrackObjectAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.info(e.getMessage());
            return "redirect:/";
        }

        var successMessage = "Successfully created API key " + developer.getKey().getKey() + " with expiration date " + developer.getKey().getExpirationDate();
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        userService.commitRegisterDeveloper(developer);
        return "redirect:/";
    }

}
