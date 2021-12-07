package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sw.laux.Studentrack.application.services.ModuleService;
import sw.laux.Studentrack.persistence.entities.Lecturer;
import sw.laux.Studentrack.persistence.entities.Major;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.UserWebShell;

@Controller
public class RegistrationController {
    @Autowired
    private ModuleService moduleService;

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

    @RequestMapping(value = "/registration/check", method = RequestMethod.POST) // th:action="@{/login}"
    public String doRegistration(Model model,
                                 @ModelAttribute("userShell") UserWebShell userShell
                                 ) {
        System.out.println(userShell);
        System.out.println(userShell.getStudent());
        return "index";
    }

}
