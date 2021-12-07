package sw.laux.Studentrack.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sw.laux.Studentrack.application.services.UserServiceInternal;

@Controller
public class LoginController {
    @Autowired
    UserServiceInternal userService;


}
