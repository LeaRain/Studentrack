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
import sw.laux.Studentrack.application.exceptions.ModuleNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.Course;
import sw.laux.Studentrack.persistence.entities.Lecturer;
import sw.laux.Studentrack.persistence.entities.User;
import sw.laux.Studentrack.persistence.entities.Module;

import java.security.Principal;
import java.util.Date;

@Controller
public class ModuleController {
    @Autowired
    private IUserServiceInternal userService;

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private Logger logger;

    @GetMapping("modules")
    public String modules(Model model, Principal principal,
                          @ModelAttribute("successMessage") String successMessage,
                          @ModelAttribute("errorMessage") String errorMessage) {

        model.addAttribute("successMessage", successMessage);
        model.addAttribute("errorMessage", errorMessage);

        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Lecturer) {
            // Harmless case, because instance is checked.
            var modules = moduleService.getAllModulesByLecturer((Lecturer) user);
            model.addAttribute("modules", modules);
        }

        model.addAttribute("faculty", user.getFaculty());
        var courseShell = new Course();
        model.addAttribute("courseShell", courseShell);
        var moduleShell = new Module();
        model.addAttribute("moduleShell", moduleShell);

        return "modules";
    }

    @GetMapping("modules/new")
    public String newModule(Model model) {
        var module = new Module();
        model.addAttribute("module", module);
        return "newmodule";
    }

    @PostMapping("modules/new/check")
    public String doNewModule(Model model,
                              Principal principal,
                              @ModelAttribute("module") Module module,
                              RedirectAttributes redirectAttributes) {

        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Lecturer) {
            module.setResponsibleLecturer((Lecturer) user);
        }

        try {
            moduleService.saveModule(module);
            var successMessage = "Successfully saved module " + module;
            redirectAttributes.addFlashAttribute("successMessage", successMessage);
            logger.info(successMessage);
        }

        catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            logger.info(exception.getMessage());
        }

        return "redirect:/modules";
    }

    @PostMapping("modules/edit")
    public String doEditModule(Model model,
                               @ModelAttribute("moduleShell") Module module,
                               RedirectAttributes redirectAttributes) {

        System.out.println(module);

        try {
            moduleService.findModule(module);

        } catch (ModuleNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.info(e.getMessage());
            return "redirect:/modules";
        }

        return "redirect:/modules/edit/" + module.getModuleId();
    }

    @GetMapping("modules/edit/{moduleId}")
    public String doEditModule(Model model,
                               @PathVariable long moduleId,
                               RedirectAttributes redirectAttributes) {
        Module module;

        try {
            module = moduleService.findModule(moduleId);
        }
        catch (ModuleNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.info(e.getMessage());
            return "redirect:/modules";
        }

        model.addAttribute(module);

        return "editmodule";
    }


    @PostMapping("modules/edit/check")
    public String doEditModuleCheck(Model model,
                                    @ModelAttribute("module") Module module,
                                    RedirectAttributes redirectAttributes) {

        System.out.println(module);

        try {
            moduleService.updateModule(module);

        } catch (ModuleNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.info(e.getMessage());
            return "redirect:/modules";
        }

        var successMessage = "Module " + module + " successfully updated!";
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        logger.info(successMessage);

        return "redirect:/modules";
    }

}
