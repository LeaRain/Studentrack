package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sw.laux.Studentrack.application.services.ModuleService;

@Controller
public class RegistrationController {
    @Autowired
    private ModuleService moduleService;

    @RequestMapping("registration")
    public String registration(Model model) {
        var faculties = moduleService.getAllFaculties();
        model.addAttribute("faculties", faculties);
        return "registration";
    }
}
