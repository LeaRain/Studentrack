package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sw.laux.Studentrack.application.services.UserServiceInternal;
import sw.laux.Studentrack.persistence.entities.UserImplementation;
import sw.laux.Studentrack.persistence.entities.UserWebShell;

@Controller
public class LoginController {
    @Autowired
    UserServiceInternal userService;

    @RequestMapping("login")
    public String login(Model model) {
        var user = new UserImplementation();
        model.addAttribute("user", user);

        return "login";
    }
}
