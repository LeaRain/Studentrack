package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sw.laux.Studentrack.application.services.UserServiceInternal;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.UserImplementation;
import sw.laux.Studentrack.persistence.entities.UserWebShell;

@Controller
public class LoginController {
    @Autowired
    private IUserServiceInternal userService;

}
