package sw.laux.Studentrack.application.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IStatisticsService;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.application.services.interfaces.IUserServiceInternal;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import java.util.*;

@Service
public class StatisticsService implements IStatisticsService {
    @Autowired
    IModuleService moduleService;

    @Autowired
    private ITimeService timeService;

    @Autowired
    private IUserServiceInternal userService;

    @Autowired
    private Logger logger;

    @Override
    public TimeDuration getTimeDurationForModuleToday(Module module) throws StudentrackObjectNotFoundException {
        return getTimeInvestDurationForTodayAndModule(module);
    }

    @Override
    public Map<Module, TimeDuration> getTimeDurationForModules(Iterable<Module> modules) {
        var moduleTimeMap = new HashMap<Module, TimeDuration>();

        for (var module : modules) {
            try {
                var timeDuration = getTimeDurationForModule(module);
                moduleTimeMap.put(module, timeDuration);
            } catch (StudentrackObjectNotFoundException ignored) {
            }
        }

        return moduleTimeMap;
    }

    @Override
    public Map<Module, TimeDuration> getTimeDurationForLecturerModules(Lecturer lecturer) {
        return getTimeDurationForModules(lecturer.getModules());
    }

    @Override
    public TimeDuration getTimeDurationForModule(Module module) throws StudentrackObjectNotFoundException {
        return getTotalTimeInvestDurationForModule(module);
    }

    @Override
    public TimeDuration getTimeInvestDurationForDateAndStudent(Date date, Student student) throws StudentrackObjectNotFoundException {
        return getTimeInvestDurationForTimeRangeAndStudent(date, date, student);
    }

    @Override
    public TimeDuration getTimeInvestDurationForTimeRangeAndStudent(Date start, Date end, Student student) throws StudentrackObjectNotFoundException {
        start = prepareInputDate(start, true);
        end = prepareInputDate(end, false);

        var timeOrders = timeService.getAllByOwnerAndStartBetween(student, start, end);

        return calculateTimeDuration(timeOrders);
    }

    @Override
    public TimeDuration calculateTimeDuration(Iterable<TimeOrder> timeOrders) {
        long durationSum = 0;
        for (var timeOrder : timeOrders) {
            var timeOrderDuration = timeOrder.getDuration();
            if (timeOrderDuration != null) {
                durationSum += timeOrderDuration.getDuration();
            }
        }

        var timeDuration = new TimeDuration();
        timeDuration.setDuration(durationSum);

        return timeDuration;
    }

    @Override
    public TimeDuration getTimeInvestDurationForTodayAndStudent(Student student) throws StudentrackObjectNotFoundException {
        return getTimeInvestDurationForDateAndStudent(new Date(), student);
    }

    @Override
    public TimeDuration getTimeInvestDurationForCurrentWeekAndStudent(Student student) throws StudentrackObjectNotFoundException {
        var dates = getWeekStartAndEnd();
        return getTimeInvestDurationForTimeRangeAndStudent(dates[0], dates[1], student);
    }

    @Override
    public TimeDuration getTimeInvestDurationForCurrentMonthAndStudent(Student student) throws StudentrackObjectNotFoundException {
        var dates = getMonthStartAndEnd();
        return getTimeInvestDurationForTimeRangeAndStudent(dates[0], dates[1], student);
    }

    @Override
    public TimeDuration getTimeInvestDurationForDateAndModule(Date date, Module module) throws StudentrackObjectNotFoundException {
        return getTimeInvestDurationForTimeRangeAndModule(date, date, module);
    }

    @Override
    public TimeDuration getTimeInvestDurationForCurrentYearAndStudent(Student student) throws StudentrackObjectNotFoundException {
        var dates = getYearStartAndEnd();
        return getTimeInvestDurationForTimeRangeAndStudent(dates[0], dates[1], student);
    }

    @Override
    public TimeDuration getTotalTimeInvestDurationForStudent(Student student) throws StudentrackObjectNotFoundException {
        var timeOrders = timeService.findAllTimeOrdersForStudent(student);
        return calculateTimeDuration(timeOrders);
    }

    @Override
    public TimeDuration getTimeInvestDurationForTimeRangeAndModule(Date start, Date end, Module module) throws StudentrackObjectNotFoundException {
        start = prepareInputDate(start, true);
        end = prepareInputDate(end, false);

        var timeOrders = timeService.getAllByModuleAndStartBetween(module, start, end);

        return calculateTimeDuration(timeOrders);
    }

    @Override
    public TimeDuration getTimeInvestDurationForTodayAndModule(Module module) throws StudentrackObjectNotFoundException {
        return getTimeInvestDurationForDateAndModule(new Date(), module);
    }

    @Override
    public TimeDuration getTotalTimeInvestDurationForModule(Module module) throws StudentrackObjectNotFoundException {
        var timeOrders = timeService.findAllTimeOrdersForModule(module);
        return calculateTimeDuration(timeOrders);
    }

    @Override
    public TimeDuration getTimeInvestDurationForCurrentWeekAndModule(Module module) throws StudentrackObjectNotFoundException {
        var dates = getWeekStartAndEnd();
        return getTimeInvestDurationForTimeRangeAndModule(dates[0], dates[1], module);
    }

    @Override
    public TimeDuration getTimeInvestDurationForCurrentMonthAndModule(Module module) throws StudentrackObjectNotFoundException {
        var dates = getMonthStartAndEnd();
        return getTimeInvestDurationForTimeRangeAndModule(dates[0], dates[1], module);
    }

    @Override
    public TimeDuration getTimeInvestDurationForCurrentYearAndModule(Module module) throws StudentrackObjectNotFoundException {
        var dates = getYearStartAndEnd();
        return getTimeInvestDurationForTimeRangeAndModule(dates[0], dates[1], module);
    }

    @Override
    public TimeDuration getTotalTimeInvestDurationForToday() throws StudentrackObjectNotFoundException {
        return getTotalTimeInvestDurationForTimeRange(new Date(), new Date());
    }

    @Override
    public TimeDuration getTotalTimeInvestDurationForTimeRange(Date start, Date end) throws StudentrackObjectNotFoundException {
        start = prepareInputDate(start, true);
        end = prepareInputDate(end, false);

        var timeOrders = timeService.getAllByStartAndEnd(start, end);

        return calculateTimeDuration(timeOrders);
    }

    @Override
    public TimeDuration getTotalTimeInvestDurationForCurrentWeek() throws StudentrackObjectNotFoundException {
        var dates = getWeekStartAndEnd();
        return getTotalTimeInvestDurationForTimeRange(dates[0], dates[1]);
    }

    @Override
    public Grade getAverageGradeForModule(Module module) throws StudentrackObjectNotFoundException {
        var results = moduleService.getResultsForModule(module);
        var averageGrade = new Grade();
        var value = calculateGradeValue(results, true);
        averageGrade.setValue(value);
        return averageGrade;
    }

    @Override
    public Grade getAverageGradeForModuleWithoutFailedGrades(Module module) throws StudentrackObjectNotFoundException {
        var results = moduleService.getResultsForModule(module);
        var averageGrade = new Grade();
        var value = calculateGradeValue(results, false);
        averageGrade.setValue(value);
        return averageGrade;
    }

    @Override
    public double calculateGradeValue(Iterable<ModuleResults> moduleResults, boolean calculateWithFailedGrade) {
        // double to prevent integer to floating point division
        var gradeSum = 0.0;
        var gradeNumber = 0;

        for (var result : moduleResults) {
            var grade = result.getGrade();
            if (grade != null && grade.getValue() != 0) {
                // Either calculate with a failed grade or without failed grades
                if (calculateWithFailedGrade || grade.getValue() != 5) {
                    gradeSum += grade.getValue();
                    gradeNumber += 1;
                }
            }
        }

        if (gradeSum == 0.0) {
            return gradeSum;
        }

        return gradeSum / gradeNumber;
    }

    @Override
    public double getFailureRateForModule(Module module) throws StudentrackObjectNotFoundException {
        var results = moduleService.getResultsForModule(module);
        int failureNumber = 0;
        int gradeNumber = 0;

        for (var result : results) {
            var grade = result.getGrade();
            if (grade != null && grade.getValue() != 0) {
                gradeNumber += 1;
                if (grade.getValue() == 5) {
                    failureNumber += 1;
                }
            }
        }

        // Cast to prevent integer division in floating-point context
        return (double) failureNumber / gradeNumber;
    }

    @Override
    public TimeDuration getAverageTimeInvestDurationForModule(Module module) throws StudentrackObjectNotFoundException {
        module = moduleService.findModule(module);
        var timeOrders = timeService.getAllByModule(module);

        var durationValue = calculateTimeDuration(timeOrders).getDuration();
        var studentNumber = module.getStudents().size();

        var timeDuration = new TimeDuration();

        if (studentNumber != 0) {
            timeDuration.setDuration(durationValue / studentNumber);
        }

        return timeDuration;
    }

    @Override
    public Grade getAverageGradeForStudent(Student student) throws StudentrackObjectNotFoundException {
        student = userService.findStudent(student);
        var results = student.getModuleResults();
        var value = calculateGradeValue(results, true);
        var grade = new Grade();
        grade.setValue(value);
        return grade;
    }

    @Override
    public Map<Student, Grade> getAverageGradeForStudents(Iterable<Student> students) {
        var studentGradeMap = new HashMap<Student, Grade>();
        for (var student : students) {
            try {
                // If an average grade for a student is not found, the student will not be part of the resulting map.
                var averageGrade = getAverageGradeForStudent(student);
                studentGradeMap.put(student, averageGrade);
            } catch (StudentrackObjectNotFoundException ignored) {}
        }

        return studentGradeMap;
    }

    @Override
    public ModuleStatisticsShell buildModuleStatisticsShell(Module module) {
        var moduleStatisticsWebShell = new ModuleStatisticsShell();
        moduleStatisticsWebShell.setModule(module);

        try {
            var timeDuration = getTimeDurationForModule(module);
            moduleStatisticsWebShell.setTotalTimeInvestDuration(timeDuration);

        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            var averageGrade = getAverageGradeForModule(module);
            moduleStatisticsWebShell.setAverageGrade(averageGrade);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            var averageGradeWithoutFailures = getAverageGradeForModuleWithoutFailedGrades(module);
            moduleStatisticsWebShell.setAverageGradeWithoutFailures(averageGradeWithoutFailures);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            var failureRate = getFailureRateForModule(module);
            moduleStatisticsWebShell.setFailureRate(failureRate);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        int numberOfStudents = module.getStudents().size();
        moduleStatisticsWebShell.setNumberOfStudents(numberOfStudents);

        try {
            var averageTimeInvestDuration = getAverageTimeInvestDurationForModule(module);
            moduleStatisticsWebShell.setAverageTimeInvestDuration(averageTimeInvestDuration);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        var estimatedTimeDuration = calculateEstimatedTimeDurationForModule(module);
        moduleStatisticsWebShell.setEstimatedTimeInvestDuration(estimatedTimeDuration);

        return moduleStatisticsWebShell;
    }

    @Override
    public Iterable<ModuleStatisticsShell> getModuleStatisticsShellsForLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException {
        var moduleStatisticsShells = new ArrayList<ModuleStatisticsShell>();
        var modules = moduleService.getAllModulesByLecturer(lecturer);

        for (var module : modules) {
            var moduleStatisticsLecturerShell = buildModuleStatisticsShell(module);
            moduleStatisticsShells.add(moduleStatisticsLecturerShell);
        }

        return moduleStatisticsShells;
    }

    @Override
    public Iterable<ModuleTimeStatisticsShell> getModuleTimeStatisticShellsForLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException {
        var moduleTimeStatisticsLecturerShells = new ArrayList<ModuleTimeStatisticsShell>();
        var modules = moduleService.getAllModulesByLecturer(lecturer);

        for (var module : modules) {
            var moduleTimeStatisticsLecturerShell = buildModuleTimeStatisticsShell(module);
            moduleTimeStatisticsLecturerShells.add(moduleTimeStatisticsLecturerShell);
        }

        return moduleTimeStatisticsLecturerShells;
    }

    @Override
    public ModuleTimeStatisticsShell buildModuleTimeStatisticsShell(Module module) {
        var moduleTimeStatisticsShell = new ModuleTimeStatisticsShell();
        moduleTimeStatisticsShell.setModule(module);

        try {
            var timeDurationToday = getTimeDurationForModuleToday(module);
            moduleTimeStatisticsShell.setTimeInvestDurationToday(timeDurationToday);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            var timeDurationWeek = getTimeInvestDurationForCurrentWeekAndModule(module);
            moduleTimeStatisticsShell.setTimeInvestDurationWeek(timeDurationWeek);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            var timeDurationMonth = getTimeInvestDurationForCurrentMonthAndModule(module);
            moduleTimeStatisticsShell.setTimeInvestDurationMonth(timeDurationMonth);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            var timeDurationYear = getTimeInvestDurationForCurrentYearAndModule(module);
            moduleTimeStatisticsShell.setTimeInvestDurationYear(timeDurationYear);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            var timeDurationTotal = getTotalTimeInvestDurationForModule(module);
            moduleTimeStatisticsShell.setTimeInvestDurationTotal(timeDurationTotal);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }


        return moduleTimeStatisticsShell;
    }

    @Override
    public TimeStatisticsShell buildTimeStatisticsShellOverviewForLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException {
        var modules = moduleService.getAllModulesByLecturer(lecturer);
        return buildTimeStatisticsShellOverviewForModules(modules);
    }

    @Override
    public ModuleStudentStatisticsShell buildModuleStudentStatisticsShell(Student student, Module module) {
        var moduleStudentStatisticsShell = new ModuleStudentStatisticsShell();
        moduleStudentStatisticsShell.setModule(module);
        try {
            var result = moduleService.collectResultForModuleOfStudent(student, module);
            moduleStudentStatisticsShell.setStudentGrade(result.getGrade());
            moduleStudentStatisticsShell.setStudentTimeInvestDuration(result.getTimeInvest().getTimeDuration());
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            var grade = getAverageGradeForModule(module);
            moduleStudentStatisticsShell.setAverageGrade(grade);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            var timeDuration = getAverageTimeInvestDurationForModule(module);
            moduleStudentStatisticsShell.setAverageTimeInvestDuration(timeDuration);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        return moduleStudentStatisticsShell;
    }

    @Override
    public Iterable<ModuleStatisticsShell> getModuleStatisticsShellForAllModules() {
        var statisticsShells = new ArrayList<ModuleStatisticsShell>();
        var modules = moduleService.findAllModules();

        for (var module : modules) {
            var shell = buildModuleStatisticsShell(module);
            statisticsShells.add(shell);
        }

        return statisticsShells;
    }

    @Override
    public Iterable<ModuleTimeStatisticsShell> getModuleTimeStatisticsForAllModules() {
        var statisticsShell = new ArrayList<ModuleTimeStatisticsShell>();
        var modules = moduleService.findAllModules();

        for (var module : modules) {
            var shell = buildModuleTimeStatisticsShell(module);
            statisticsShell.add(shell);
        }

        return statisticsShell;
    }

    @Override
    public Iterable<ModuleStudentStatisticsShell> getModuleStudentStatisticsForStudent(Student student) {
        var statisticShells = new ArrayList<ModuleStudentStatisticsShell>();
        var modules = student.getModules();

        for (var module : modules) {
            var shell = buildModuleStudentStatisticsShell(student, module);
            statisticShells.add(shell);
        }

        return statisticShells;
    }

    @Override
    public TimeStatisticsShell getModuleTimeStatisticsOverviewForAllModules() {
        var modules = moduleService.findAllModules();
        return buildTimeStatisticsShellOverviewForModules(modules);
    }

    @Override
    public TimeStatisticsShell buildTimeStatisticsShellOverviewForModules(Iterable<Module> modules) {
        var moduleTimeStatisticsShell = new ModuleTimeStatisticsShell();

        var timeDurationToday = new TimeDuration();
        var timeDurationWeek = new TimeDuration();
        var timeDurationMonth = new TimeDuration();
        var timeDurationYear = new TimeDuration();
        var timeDurationTotal = new TimeDuration();

        for (var module : modules) {
            try {
                var moduleTimeDurationToday = getTimeDurationForModuleToday(module);
                var duration = timeDurationToday.getDuration() + moduleTimeDurationToday.getDuration();
                timeDurationToday.setDuration(duration);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            try {
                var moduleTimeDurationWeek = getTimeInvestDurationForCurrentWeekAndModule(module);
                var duration = timeDurationWeek.getDuration() + moduleTimeDurationWeek.getDuration();
                timeDurationWeek.setDuration(duration);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            try {
                var moduleTimeDurationMonth = getTimeInvestDurationForCurrentMonthAndModule(module);
                var duration = timeDurationMonth.getDuration() + moduleTimeDurationMonth.getDuration();
                timeDurationMonth.setDuration(duration);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            try {
                var moduleTimeDurationYear = getTimeInvestDurationForCurrentYearAndModule(module);
                var duration = timeDurationYear.getDuration() + moduleTimeDurationYear.getDuration();
                timeDurationYear.setDuration(duration);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }

            try {
                var moduleTimeDurationTotal = getTotalTimeInvestDurationForModule(module);
                var duration = timeDurationTotal.getDuration() + moduleTimeDurationTotal.getDuration();
                timeDurationTotal.setDuration(duration);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }
        }

        moduleTimeStatisticsShell.setTimeInvestDurationToday(timeDurationToday);
        moduleTimeStatisticsShell.setTimeInvestDurationWeek(timeDurationWeek);
        moduleTimeStatisticsShell.setTimeInvestDurationMonth(timeDurationMonth);
        moduleTimeStatisticsShell.setTimeInvestDurationYear(timeDurationYear);
        moduleTimeStatisticsShell.setTimeInvestDurationTotal(timeDurationTotal);

        return moduleTimeStatisticsShell;
    }

    @Override
    public TimeStatisticsShell getTimeStatisticsOverviewForStudent(Student student) {
        var timeStatisticsShell = new TimeStatisticsShell();
        var timeDurationToday = new TimeDuration();
        var timeDurationWeek = new TimeDuration();
        var timeDurationMonth = new TimeDuration();
        var timeDurationYear = new TimeDuration();
        var timeDurationTotal = new TimeDuration();

        try {
            timeDurationToday = getTimeInvestDurationForTodayAndStudent(student);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            timeDurationWeek = getTimeInvestDurationForCurrentWeekAndStudent(student);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            timeDurationMonth = getTimeInvestDurationForCurrentMonthAndStudent(student);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            timeDurationYear = getTimeInvestDurationForCurrentYearAndStudent(student);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        try {
            timeDurationTotal = getTotalTimeInvestDurationForStudent(student);
        } catch (StudentrackObjectNotFoundException e) {
            logger.info(e.getMessage());
        }

        timeStatisticsShell.setTimeInvestDurationToday(timeDurationToday);
        timeStatisticsShell.setTimeInvestDurationWeek(timeDurationWeek);
        timeStatisticsShell.setTimeInvestDurationMonth(timeDurationMonth);
        timeStatisticsShell.setTimeInvestDurationYear(timeDurationYear);
        timeStatisticsShell.setTimeInvestDurationTotal(timeDurationTotal);

        return timeStatisticsShell;
    }

    @Override
    public ModuleEstimationStatisticsShell getCurrentProgressOfStudentInModule(Student student, Module module) throws StudentrackObjectNotFoundException {
        var shell = new ModuleEstimationStatisticsShell();
        shell.setModule(module);
        // Module passed -> student done
        if (moduleService.hasStudentPassedModule(student, module)) {
            shell.setCurrentPercentage(1);
        }

        var timeOrders = timeService.findTimeOrdersForModuleAndStudent(module, student);
        var timeDuration = calculateTimeDuration(timeOrders);
        shell.setCurrentDuration(timeDuration);
        var estimatedTimeDuration = calculateEstimatedTimeDurationForModule(module);
        shell.setEstimatedDuration(estimatedTimeDuration);

        var percentage = (double) timeDuration.getDuration() / estimatedTimeDuration.getDuration();
        percentage = Math.round(percentage * 10000.0) / 10000.0;

        // More time than currently planned -> set to 1
        if (percentage > 1) {
            percentage = 1;
        }

        shell.setCurrentPercentage(percentage);

        return shell;
    }

    @Override
    public Iterable<ModuleEstimationStatisticsShell> getCurrentProgressOfStudentInAllModules(Student student) {
        var statisticsShells = new ArrayList<ModuleEstimationStatisticsShell>();
        var modules = student.getModules();
        for (var module : modules) {
            try {
                var shell = getCurrentProgressOfStudentInModule(student, module);
                statisticsShells.add(shell);
            } catch (StudentrackObjectNotFoundException e) {
                logger.info(e.getMessage());
            }
        }
        return statisticsShells;
    }

    @Override
    public TimeDuration calculateEstimatedTimeDurationForModule(Module module) {
        var ects = module.getEcts();
        // One ECTS = 30 hours = 108000000 milliseconds
        long estimatedValue = ects * 108000000L;
        var timeDuration = new TimeDuration();
        timeDuration.setDuration(estimatedValue);
        return timeDuration;
    }

    @Override
    public Map<ModuleDTO, GradeDTO> getModuleGradesForStudent(Student student) throws StudentrackObjectNotFoundException {
        var results = moduleService.collectResultsForAllModulesOfStudent(student);
        var resultMap = new HashMap<ModuleDTO, GradeDTO>();
        for (var result : results) {
            var module = result.getModule();
            var moduleDto = buildModuleDTOBasedOnModule(module);
            var grade = result.getGrade();
            var gradeDto = buildGradeDTOBasedOnGrade(grade);
            resultMap.put(moduleDto, gradeDto);
        }

        return resultMap;
    }

    @Override
    public Map<ModuleDTO, TimeDurationDTO> getModuleTimeDurationForStudent(Student student) throws StudentrackObjectNotFoundException {
        var results = moduleService.collectResultsForAllModulesOfStudent(student);
        var resultMap = new HashMap<ModuleDTO, TimeDurationDTO>();
        for (var result : results) {
            var module = result.getModule();
            var timeDuration = result.getTimeInvest().getTimeDuration();
            var moduleDto = buildModuleDTOBasedOnModule(module);
            var timeDurationDto = buildTimeDurationDTOBasedOnTimeDuration(timeDuration);
            resultMap.put(moduleDto, timeDurationDto);
        }

        return resultMap;
    }

    @Override
    public ModuleDTO buildModuleDTOBasedOnModule(Module module) {
        var moduleDto = new ModuleDTO();
        moduleDto.setModuleId(module.getModuleId());
        moduleDto.setName(module.getName());
        moduleDto.setEcts(module.getEcts());
        moduleDto.setCreditHours(module.getCreditHours());
        return moduleDto;
    }

    @Override
    public GradeDTO buildGradeDTOBasedOnGrade(Grade grade) {
        if (grade == null) {
            return null;
        }

        var gradeDto = new GradeDTO();
        gradeDto.setGradeId(grade.getGradeId());
        gradeDto.setValue(grade.getValue());
        gradeDto.setDescription(grade.getDescription());
        gradeDto.setTryNumber(grade.getTryNumber());
        return gradeDto;
    }

    @Override
    public TimeDurationDTO buildTimeDurationDTOBasedOnTimeDuration(TimeDuration timeDuration) {
        var timeDurationDto = new TimeDurationDTO();
        timeDurationDto.setDuration(timeDuration.getDuration());
        return timeDurationDto;
    }

    @Override
    public Iterable<ModuleDTO> getAllModules() {
        var results = new ArrayList<ModuleDTO>();
        var modules = moduleService.getAllModules();
        for (var module : modules) {
            var moduleDto = buildModuleDTOBasedOnModule(module);
            results.add(moduleDto);
        }

        return results;
    }

    @Override
    public ModuleDTO getModuleDTOBasedOnModuleId(long moduleId) throws StudentrackObjectNotFoundException {
        var module = moduleService.findModule(moduleId);
        return buildModuleDTOBasedOnModule(module);
    }

    @Override
    public GradeDTO getAverageGradeDTOForModule(long moduleId) throws StudentrackObjectNotFoundException {
        var module = moduleService.findModule(moduleId);
        var grade = getAverageGradeForModule(module);
        return buildGradeDTOBasedOnGrade(grade);
    }

    @Override
    public TimeDurationDTO getAverageTimeDurationDTOForModule(long moduleId) throws StudentrackObjectNotFoundException {
        var module = moduleService.findModule(moduleId);
        var timeDuration = getAverageTimeInvestDurationForModule(module);
        return buildTimeDurationDTOBasedOnTimeDuration(timeDuration);
    }

    @Override
    public Date[] getWeekStartAndEnd() {
        var dateResult = new Date[2];

        var calendar = new GregorianCalendar();

        while(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        var weekStart = calendar.getTime();
        dateResult[0] = weekStart;

        while(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        var weekEnd = calendar.getTime();
        dateResult[1] = weekEnd;

        return dateResult;
    }

    @Override
    public Date[] getMonthStartAndEnd() {
        var dateResult = new Date[2];

        var calendar = new GregorianCalendar();
        var firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        var firstDate = calendar.getTime();
        dateResult[0] = firstDate;
        var lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        var lastDate = calendar.getTime();
        dateResult[1] = lastDate;

        return dateResult;
    }

    @Override
    public Date[] getYearStartAndEnd() {
        var dateResult = new Date[2];

        var calendar = new GregorianCalendar();
        var firstDay = calendar.getActualMinimum(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, firstDay);
        var firstDate = calendar.getTime();
        dateResult[0] = firstDate;
        var lastDay = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DAY_OF_YEAR, lastDay);
        var lastDate = calendar.getTime();
        dateResult[1] = lastDate;

        return dateResult;
    }

    @Override
    public Date prepareInputDate(Date date, boolean back) {
        date = setTimeToMidnight(date);
        if (back) {
            date = new Date(date.getTime() - 1);
        }

        else {
            date = new Date(date.getTime() + 86400000);
        }
        return date;
    }

    @Override
    public Date setTimeToMidnight(Date date) {
        var calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
