package sw.laux.Studentrack.presentation.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sw.laux.Studentrack.persistence.entities.Module;

@Controller
public class TimeController {
    @Autowired
    private Logger logger;


    @PostMapping("/timeorders/new")
    public String startNewTimeOrder(Model model,
                                    @ModelAttribute("moduleShell") Module module) {

        System.out.println(module);

        return "redirect:/home";

    }
}
