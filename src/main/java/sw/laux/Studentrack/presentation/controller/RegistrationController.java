package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sw.laux.Studentrack.application.services.ModuleService;
import sw.laux.Studentrack.application.services.UserService;
import sw.laux.Studentrack.application.services.UserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;

@Controller
public class RegistrationController {
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private UserServiceInternal userService;

    @RequestMapping("registration")
    public String registration(Model model) {
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

    @RequestMapping(value = "/registration/check", method = RequestMethod.POST)
    public String doRegistration(Model model,
                                 @ModelAttribute("userShell") UserWebShell userShell
                                 ) {

        User user = null;

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

        // TODO: Success and error 
        System.out.println(userService.registerUser(user));
        return "index";
    }

}
