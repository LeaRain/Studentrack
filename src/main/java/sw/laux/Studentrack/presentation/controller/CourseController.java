package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.Lecturer;
import sw.laux.Studentrack.persistence.entities.User;

import java.security.Principal;

@Controller
public class CourseController {
    @Autowired
    private IUserServiceInternal userService;

    @Autowired
    private IModuleService moduleService;

    @GetMapping("courses")
    public String courses(Model model, Principal principal) {
        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Lecturer) {
            // Harmless case, because instance is checked.
            var courses = moduleService.getAllCoursesByLecturer((Lecturer) user);
            model.addAttribute("courses", courses);

        }

        return "courses";
    }
}
