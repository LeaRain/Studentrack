package sw.laux.Studentrack.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
public class StartController {
    @RequestMapping("/")
    public String index(Model model,
                        @ModelAttribute("successMessage") String successMessage) {
        if (!Objects.equals(successMessage, "")) {
            model.addAttribute("successMessage", successMessage);
        }
        return "index";
    }

}
