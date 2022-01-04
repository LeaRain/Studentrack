package sw.laux.Studentrack.presentation.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.Lecturer;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.User;

import java.security.Principal;

@Controller
public class UserController {
    @Autowired
    private IModuleService moduleService;
    @Autowired
    private IUserServiceInternal userService;
    @Autowired
    private Logger logger;
    @Autowired
    private ITimeService timeService;

    @GetMapping("home")
    public String home(Model model, Principal principal) {
        var user = (User) userService.loadUserByUsername(principal.getName());

        if (user instanceof Student) {
            model.addAttribute("modules", (moduleService.findCurrentlyNotPassedModulesForStudent((Student) user)));
            model.addAttribute("moduleShell", new Module());
            try {
                var timeOrder = timeService.findOpenTimeOrderForStudent((Student) user);
                model.addAttribute("timeOrder", timeOrder);
            } catch (StudentrackObjectNotFoundException ignored) {
            }

            try {
                var moduleResults = moduleService.collectResultsForAllModulesOfStudent((Student) user);
                model.addAttribute("moduleResults", moduleResults);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            try {
                var timeDurationToday = timeService.getTimeInvestDurationForTodayAndStudent((Student) user);
                model.addAttribute("timeDurationToday", timeDurationToday);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            try {
                var timeDurationWeek = timeService.getTimeInvestDurationForCurrentWeekAndStudent((Student) user);
                model.addAttribute("timeDurationWeek", timeDurationWeek);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }
        }

        if (user instanceof Lecturer) {
            var modulesWithTimeDuration = moduleService.getTimeDurationForLecturerModules((Lecturer) user);
            model.addAttribute("modulesWithTimeDuration", modulesWithTimeDuration);

            try {
                var timeDurationToday = timeService.getTotalTimeInvestDurationForToday();
                model.addAttribute("timeDurationToday", timeDurationToday);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            try {
                var timeDurationWeek = timeService.getTotalTimeInvestDurationForCurrentWeek();
                model.addAttribute("timeDurationWeek", timeDurationWeek);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }
        }

        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("change")
    public String changeUserData(Model model, Principal principal) {
        return "change";
    }
}
