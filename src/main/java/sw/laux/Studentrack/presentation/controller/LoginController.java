package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "login/check", method = RequestMethod.POST)
    public String doLogin(Model model,
                          @ModelAttribute("user") UserImplementation user) {

        return "redirect:/home";

    }

    @RequestMapping(value = "home")
    public String home(Model model) {
        return "home";
    }
}
