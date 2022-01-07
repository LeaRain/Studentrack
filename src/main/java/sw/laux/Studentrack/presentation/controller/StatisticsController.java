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
    private Logger logger;

    @GetMapping("statistics")
    public String statistics(Model model,
                             Principal principal) {

        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Lecturer lecturer) {
            Iterable<ModuleStatisticsShell> moduleStatisticsLecturerShells = null;
            try {
                moduleStatisticsLecturerShells = statisticsService.getModuleStatisticsShellsForLecturer(lecturer);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            Iterable<ModuleTimeStatisticsShell> moduleTimeStatisticsLecturerShells = null;
            try {
                moduleTimeStatisticsLecturerShells = statisticsService.getModuleTimeStatisticShellsForLecturer(lecturer);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }


            model.addAttribute("moduleStatisticsLecturer", moduleStatisticsLecturerShells);
            model.addAttribute("moduleTimeStatisticsLecturer",  moduleTimeStatisticsLecturerShells);

            ModuleTimeStatisticsShell moduleTimeStatisticsOverviewLecturer = null;
            try {
                moduleTimeStatisticsOverviewLecturer = statisticsService.buildModuleTimeStatisticsShellOverviewForLecturer(lecturer);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }
            model.addAttribute("moduleTimeStatisticsOverviewLecturer", moduleTimeStatisticsOverviewLecturer);
        }

        if (user instanceof Student student) {
            // TODO: Student statistics, separate into free and premium
        }

        model.addAttribute("moduleStatistics", statisticsService.getModuleStatisticsShellForAllModules());
        model.addAttribute("moduleTimeStatistics", statisticsService.getModuleTimeStatisticsForAllModules());
        model.addAttribute("moduleTimeStatisticsOverview", statisticsService.getModuleTimeStatisticsOverviewForAllModules());

        return "statistics";
    }
}
