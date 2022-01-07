package sw.laux.Studentrack.presentation.controller;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IStatisticsService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Objects;

@Controller
public class StatisticsController {
    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private IUserServiceInternal userService;

    @Autowired
    private IModuleService moduleService;

    @Autowired
    private Logger logger;

    @GetMapping("statistics")
    public String statistics(Model model,
                             Principal principal) {

        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Lecturer lecturer) {
            var moduleStatisticsLecturerWebShells = new ArrayList<ModuleStatisticsShell>();
            var moduleTimeStatisticsLecturerWebShells = new ArrayList<ModuleTimeStatisticsShell>();
            Iterable<Module> modules = null;
            try {
                modules = moduleService.getAllModulesByLecturer(lecturer);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            for (var module : Objects.requireNonNull(modules)) {
                var moduleStatisticsLecturerWebShell = statisticsService.buildModuleStatisticsShell(module);
                moduleStatisticsLecturerWebShells.add(moduleStatisticsLecturerWebShell);
                var moduleTimeStatisticsLecturerWebShell = statisticsService.buildModuleTimeStatisticsShell(module);
                moduleTimeStatisticsLecturerWebShells.add(moduleTimeStatisticsLecturerWebShell);
            }

            model.addAttribute("moduleStatisticsLecturer", moduleStatisticsLecturerWebShells);
            model.addAttribute("moduleTimeStatisticsLecturer", moduleTimeStatisticsLecturerWebShells);

            var moduleTimeStatisticsOverviewLecturer = statisticsService.buildModuleTimeStatisticsShellOverviewForLecturer(lecturer);
            model.addAttribute("moduleTimeStatisticsOverviewLecturer", moduleTimeStatisticsOverviewLecturer);

        }

        return "statistics";
    }
}
