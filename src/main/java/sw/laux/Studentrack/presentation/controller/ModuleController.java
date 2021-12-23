package sw.laux.Studentrack.presentation.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw.laux.Studentrack.application.exceptions.*;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import java.security.Principal;

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

        if (user instanceof Student) {
            var major = ((Student) user).getMajor();
            model.addAttribute("major", major);
            var modules = ((Student) user).getModules();
            model.addAttribute("modules", modules);
        }

        if (user instanceof Lecturer) {
            Iterable<Module> modules = null;
            try {
                // Harmless case, because instance is checked.
                modules = moduleService.getAllModulesByLecturer((Lecturer) user);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }
            model.addAttribute("modules", modules);
        }

        model.addAttribute("faculty", user.getFaculty());
        var moduleShell = new Module();
        model.addAttribute("moduleShell", moduleShell);

        return "modules";
    }

    @GetMapping("modules/new")
    public String newModule(Model model,
                            Principal principal) {

        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Student) {
            var modules = moduleService.getNonTakenModulesByStudent((Student) user);

            model.addAttribute("modules", modules);
            model.addAttribute("faculty", user.getFaculty());
        }

        var module = new Module();
        model.addAttribute("moduleShell", module);
        return "newmodule";
    }

    @PostMapping("modules/new/check")
    public String doNewModule(Model model,
                              Principal principal,
                              @ModelAttribute("moduleShell") Module moduleShell,
                              RedirectAttributes redirectAttributes) {

        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Lecturer) {
            moduleShell.setResponsibleLecturer((Lecturer) user);
            try {
                moduleService.saveNewModule(moduleShell);
                var successMessage = "Successfully saved module " + moduleShell;
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
                logger.info(successMessage);
            }

            catch (StudentrackObjectAlreadyExistsException exception) {
                redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
                logger.info(exception.getMessage());
            }
        }

        if (user instanceof Student) {
            try {
                var module = moduleService.enrollInModule((Student) user, moduleShell);
                var successMessage = "Successfully enrolled " + user + " in module " + module;
                redirectAttributes.addFlashAttribute("successMessage", successMessage);
                logger.info(successMessage);

            } catch (StudentrackObjectAlreadyExistsException | StudentrackObjectNotFoundException exception) {
                redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
                logger.info(exception.getMessage());
            }
        }

        return "redirect:/modules";
    }

    @PostMapping("modules/edit")
    public String doEditModule(Model model,
                               @ModelAttribute("moduleShell") Module module,
                               RedirectAttributes redirectAttributes) {

        try {
            moduleService.findModule(module);

        } catch (StudentrackObjectNotFoundException e) {
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
        catch (StudentrackObjectNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.info(e.getMessage());
            return "redirect:/modules";
        }

        model.addAttribute("module", module);

        return "editmodule";
    }

    @PostMapping("modules/edit/check")
    public String doEditModuleCheck(Model model,
                                    @ModelAttribute("module") Module module,
                                    RedirectAttributes redirectAttributes) {

        try {
            moduleService.updateModule(module);

        } catch (StudentrackObjectNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.info(e.getMessage());
            return "redirect:/modules";
        }

        var successMessage = "Module " + module + " successfully updated!";
        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        logger.info(successMessage);

        return "redirect:/modules";
    }

    @PostMapping("modules/withdraw")
    public String doWithdrawModule(Model model,
                               Principal principal,
                               @ModelAttribute("moduleShell") Module module,
                               RedirectAttributes redirectAttributes) {

        var user = (User) userService.loadUserByUsername(principal.getName());

        try {
            // Cast possible due to check for authorities with Spring Security and access only for Students
            module = moduleService.withdrawFromModule((Student) user, module);
            var successMessage = "Successfully withdrawn from module " + module;
            logger.info(successMessage);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (StudentrackObjectNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            logger.info(e.getMessage());
            return "redirect:/modules";
        }

        return "redirect:/modules";
    }

    @PostMapping("modules/delete")
    public String doDeleteModule(Model model,
                                 @ModelAttribute("module") Module module,
                                 RedirectAttributes redirectAttributes) {

        try {
            moduleService.deleteModule(module);
            var successMessage = "Successfully deleted module " + module;
            logger.info(successMessage);
            redirectAttributes.addFlashAttribute("successMessage", successMessage);

        } catch (StudentrackObjectCannotBeDeletedException | StudentrackObjectNotFoundException exception) {
            logger.info(exception.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }
        return "redirect:/modules";
    }

}
