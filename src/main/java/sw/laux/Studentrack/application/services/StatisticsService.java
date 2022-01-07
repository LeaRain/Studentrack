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
    public Map<Module, TimeDuration> getTimeDurationForModulesToday(Iterable<Module> modules) {
        var moduleTimeMap = new HashMap<Module, TimeDuration>();

        for (var module : modules) {
            try {
                var timeDuration = getTimeDurationForModuleToday(module);
                moduleTimeMap.put(module, timeDuration);
            } catch (StudentrackObjectNotFoundException ignored) {
            }
        }

        return moduleTimeMap;
    }

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
        var results = moduleService.getResultsForModule(module);
        var timeDurationSum = 0;
        var timeNumber = 0;

        for (var result : results) {
            var timeInvest = result.getTimeInvest();
            if (timeInvest != null) {
                var timeDuration = timeInvest.getTimeDuration();
                if (timeDuration != null) {
                    timeDurationSum += timeDuration.getDuration();
                    timeNumber += 1;
                }
            }
        }

        var duration = new TimeDuration();
        duration.setDuration(timeDurationSum / timeNumber);

        return duration;
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
