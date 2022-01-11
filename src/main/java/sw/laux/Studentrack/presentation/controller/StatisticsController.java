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
import sw.laux.Studentrack.presentation.WebShell.ModuleStatisticsShell;
import sw.laux.Studentrack.presentation.WebShell.ModuleTimeStatisticsShell;
import sw.laux.Studentrack.presentation.WebShell.TimeStatisticsShell;

import java.security.Principal;

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

        // Determine access to specific parts of the statistics
        boolean overallAccess = false;
        if (user instanceof Lecturer) {
            overallAccess = true;
        }
        else {
            if (user instanceof Student student && student.isPremiumUser()) {
                overallAccess = true;
            }
        }

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

            TimeStatisticsShell moduleTimeStatisticsOverviewLecturer = null;
            try {
                moduleTimeStatisticsOverviewLecturer = statisticsService.buildTimeStatisticsShellOverviewForLecturer(lecturer);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }
            model.addAttribute("moduleTimeStatisticsOverviewLecturer", moduleTimeStatisticsOverviewLecturer);
        }

        if (user instanceof Student student) {
            try {
                var moduleResults = moduleService.collectResultsForAllModulesOfStudent(student);
                model.addAttribute("moduleResults", moduleResults);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            model.addAttribute("isPremium", student.isPremiumUser());

            if (student.isPremiumUser()) {
                var timeStatisticsOverviewStudent = statisticsService.getTimeStatisticsOverviewForStudent(student);
                model.addAttribute("timeStatisticsOverview", timeStatisticsOverviewStudent);
                var moduleStudentStatisticsShells = statisticsService.getModuleStudentStatisticsForStudent(student);
                model.addAttribute("moduleStudentStatistics", moduleStudentStatisticsShells);
            }

        }

        if (overallAccess) {
            model.addAttribute("moduleStatistics", statisticsService.getModuleStatisticsShellForAllModules());
            model.addAttribute("moduleTimeStatistics", statisticsService.getModuleTimeStatisticsForAllModules());
            model.addAttribute("moduleTimeStatisticsOverview", statisticsService.getModuleTimeStatisticsOverviewForAllModules());
        }

        return "statistics";
    }
}
