package sw.laux.Studentrack.presentation.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectAlreadyExistsException;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

        var successMessage = "Successfully updated Time Order " + timeOrder + " for " + timeOrder.getModule() + " for student " + user;
        logger.info(successMessage);
        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return "redirect:/home";
    }

    @GetMapping("timeorders")
    public String timeorders(Model model,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {

        var user = userService.loadUserByUsername(principal.getName());

        if (!(user instanceof Student)) {
            var errorMessage = "Wrong user type for " + user;
            logger.info(errorMessage);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/home";
        }

        Iterable<TimeOrder> timeOrders = null;

        try {
            timeOrders = timeService.getAllTimeOrdersForStudent((Student) user);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        model.addAttribute("timeorders", timeOrders);
        model.addAttribute("timeorderShell", new TimeOrder());

        return "timeorders";
    }

    @GetMapping("timeorders/new")
    public String newTimeorder(Model model,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {

        var user = userService.loadUserByUsername(principal.getName());

        if (user instanceof Student) {
            var modules = ((Student) user).getModules();
            model.addAttribute("modules", modules);
        }

        var timeOrderShell = new TimeOrderWebShell();
        model.addAttribute("timeOrderShell", timeOrderShell);

        return "newtimeorder";
    }

    @PostMapping("timeorders/new/check")
    public String doNewTimeorder(Model model,
                              Principal principal,
                              @ModelAttribute("timeOrderShell") TimeOrderWebShell timeOrderShell,
                              RedirectAttributes redirectAttributes) {

        // Inspiration by https://www.baeldung.com/java-date-to-localdate-and-localdatetime
        var startDate = Date.from(
                LocalDateTime.parse(timeOrderShell.getStartString())
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );

        var endDate = Date.from(
                LocalDateTime.parse(timeOrderShell.getEndString())
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );

        var timeOrder = timeOrderShell.getTimeOrder();
        timeOrder.setStart(startDate);
        timeOrder.setEnd(endDate);
        var user = userService.loadUserByUsername(principal.getName());
        timeOrder.setOwner((Student) user);
        timeOrder = timeService.saveTimeOrder(timeOrder);

        var successMessage = "Time Order " + timeOrder + "saved!";
        logger.info(successMessage);
        redirectAttributes.addFlashAttribute("successMessage", successMessage);

        return "redirect:/timeorders";
    }

    @PostMapping("timeorders/edit")
    public String doEditTimeorder(Model model,
                               @ModelAttribute("timeOrderShell") TimeOrder timeOrder,
                               RedirectAttributes redirectAttributes) {

        try {
            timeService.findTimeOrder(timeOrder);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/timeorders";
        }

        return "redirect:/timeorders/edit/" + timeOrder.getTimeOrderId();
    }

    @GetMapping("timeorders/edit/{timeOrderId}")
    public String doEditTimeOrder(Model model,
                               @PathVariable long timeOrderId,
                               RedirectAttributes redirectAttributes) {
        TimeOrder timeOrder;

        try {
            timeOrder = timeService.findTimeOrder(timeOrderId);
        }
        catch (StudentrackObjectNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.info(e.getMessage());
            return "redirect:/modules";
        }

        var timeOrderShell = new TimeOrderWebShell();
        timeOrderShell.setTimeOrder(timeOrder);
        // Inspiration by https://www.baeldung.com/java-date-to-localdate-and-localdatetime
        var localDateTimeStart = timeOrder.getStart().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
        var localDateTimeEnd = timeOrder.getEnd().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        timeOrderShell.setStartString(String.valueOf(localDateTimeStart));
        timeOrderShell.setEndString(String.valueOf(localDateTimeEnd));


        model.addAttribute("timeOrder", timeOrderShell);

        return "edittimeorder";
    }

    @PostMapping("timeorders/edit/check")
    public String doEditModuleCheck(Model model,
                                    @ModelAttribute("timeOrder") TimeOrderWebShell timeOrderWebShell,
                                    RedirectAttributes redirectAttributes) {
        //TODO: Update time order

        System.out.println(timeOrderWebShell.getStartString());
        System.out.println(timeOrderWebShell.getEndString());
        System.out.println(timeOrderWebShell.getTimeOrder().getTimeOrderId());
        System.out.println(timeOrderWebShell.getTimeOrder().getModule());
        System.out.println(timeOrderWebShell.getTimeOrder().getOwner());
        return "redirect:/timeorders";
    }
}
