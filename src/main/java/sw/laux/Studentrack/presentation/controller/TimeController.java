package sw.laux.Studentrack.presentation.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.TimeOrder;
import sw.laux.Studentrack.persistence.entities.User;

import java.security.Principal;
import java.util.Date;

@Controller
public class TimeController {
    @Autowired
    private Logger logger;

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private ITimeService timeService;

    @Autowired
    private IUserServiceInternal userService;


    @PostMapping("/timeorders/start")
    public String startNewTimeOrder(Model model,
                                    @ModelAttribute("moduleShell") Module module,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {

        try {
            module = moduleService.findModule(module);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/home";
        }

        var user = (User) userService.loadUserByUsername(principal.getName());

        if (!(user instanceof Student)) {
            var errorMessage = "Wrong user type for " + user;
            logger.info(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/home";
        }

        var timeOrder = new TimeOrder();
        timeOrder.setStart(new Date());
        timeOrder.setOwner((Student) user);
        timeOrder.setModule(module);

        try {
            timeOrder = timeService.createOpenTimeOrder(timeOrder);
        } catch (StudentrackObjectAlreadyExistsException e) {
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/home";
        }

        var successMessage = "Successfully created a new time order " + timeOrder + " for " + module + " for student " + user;
        logger.info(successMessage);
        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return "redirect:/home";

    }

    @PostMapping("/timeorders/end")
    public String endCurrentTimeOrder(Model model,
                                      @ModelAttribute("timeOrder") TimeOrder timeOrder,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {


        var user = userService.loadUserByUsername(principal.getName());

        if (!(user instanceof Student)) {
            var errorMessage = "Wrong user type for " + user;
            logger.info(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/home";
        }

        timeOrder.setEnd(new Date());

        try {
            timeOrder = timeService.closeOpenTimeOrderForStudent(timeOrder, (Student) user);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/home";
        }

        var successMessage = "Successfully updated Time Order " + timeOrder;
        logger.info(successMessage);
        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return "redirect:/home";
    }
}
