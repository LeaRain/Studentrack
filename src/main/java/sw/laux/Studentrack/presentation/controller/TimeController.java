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
import sw.laux.Studentrack.application.exceptions.StudentrackOperationNotAllowedException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                                    RedirectAttributes redirectAttributes ) {

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
        timeOrder.setStart(getCurrentDateTime());
        timeOrder.setOwner((Student) user);
        timeOrder.setModule(module);

        try {
            timeOrder = timeService.createOpenTimeOrder(timeOrder);
        } catch (StudentrackObjectAlreadyExistsException | StudentrackOperationNotAllowedException e) {
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

        timeOrder.setEnd(getCurrentDateTime());

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
        // TODO: Change to modules without grade
        model.addAttribute("modules", ((Student) user).getModules());
        model.addAttribute("moduleShell", new Module());
        try {
            var timeOrder = timeService.findOpenTimeOrderForStudent((Student) user);
            model.addAttribute("timeOrder", timeOrder);
        } catch (StudentrackObjectNotFoundException ignored) {
        }

        return "timeorders";
    }

    @GetMapping("timeorders/new")
    public String newTimeorder(Model model,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {

        var user = userService.loadUserByUsername(principal.getName());

        if (user instanceof Student) {
            // TODO: Change to modules without grade
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

        var startDate = frontendDateStringToDate(timeOrderShell.getStartString());
        var endDate = frontendDateStringToDate(timeOrderShell.getEndString());

        var timeOrder = timeOrderShell.getTimeOrder();
        timeOrder.setStart(startDate);
        timeOrder.setEnd(endDate);
        var user = userService.loadUserByUsername(principal.getName());
        timeOrder.setOwner((Student) user);

        try {
            timeOrder = timeService.saveTimeOrder(timeOrder);
        } catch (StudentrackOperationNotAllowedException e) {
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/timeorders";
        }

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

        if (timeOrder.getStart() != null) {
            var localDateTimeStart = timeOrderDateToLocalDateTime(timeOrder.getStart());
            timeOrderShell.setStartString(String.valueOf(localDateTimeStart));
        }

        if (timeOrder.getEnd() != null) {
            var localDateTimeEnd = timeOrderDateToLocalDateTime(timeOrder.getEnd());
            timeOrderShell.setEndString(String.valueOf(localDateTimeEnd));
        }

        model.addAttribute("timeOrder", timeOrderShell);

        return "edittimeorder";
    }

    @PostMapping("timeorders/edit/check")
    public String doEditModuleCheck(Model model,
                                    @ModelAttribute("timeOrder") TimeOrderWebShell timeOrderWebShell,
                                    RedirectAttributes redirectAttributes) {
        var timeOrder = timeOrderWebShell.getTimeOrder();
        var startDate = frontendDateStringToDate(timeOrderWebShell.getStartString());
        var endDate = frontendDateStringToDate(timeOrderWebShell.getEndString());
        timeOrder.setStart(startDate);
        timeOrder.setEnd(endDate);
        try {
            timeService.updateTimeOrder(timeOrder);
            var successMessage = "Successfully updated: " + timeOrder;
            logger.info(successMessage);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/timeorders";
    }

    @PostMapping("timeorders/delete")
    public String doDeleteTimeOrder(Model model,
                                   @ModelAttribute("timeOrder") TimeOrderWebShell timeOrderShell,
                                   RedirectAttributes redirectAttributes) {

        var timeOrder = timeOrderShell.getTimeOrder();
        try {
            timeService.deleteTimeOrder(timeOrder);
            var successMessage = "Successfully deleted the time order with id " + timeOrder.getTimeOrderId() + "!";
            logger.info(successMessage);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/timeorders";
    }

    @PostMapping("timeorders/cancel")
    public String doCancelTimeOrder(Model model,
                                    @ModelAttribute("timeOrder") TimeOrderWebShell timeOrderShell,
                                    RedirectAttributes redirectAttributes) {

        return doDeleteTimeOrder(model, timeOrderShell, redirectAttributes);
    }

    // Inspiration by https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    public Date frontendDateStringToDate(String dateString) {
        return Date.from(
                LocalDateTime.parse(dateString)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );
    }

    // Inspiration by https://www.baeldung.com/java-date-to-localdate-and-localdatetime
    public LocalDateTime timeOrderDateToLocalDateTime(Date timeOrderDate) {
        return timeOrderDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Date getCurrentDateTime() {
        var dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        Date date ;
        try {
            date = dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
            date = new Date();
        }
        return date;
    }
}
