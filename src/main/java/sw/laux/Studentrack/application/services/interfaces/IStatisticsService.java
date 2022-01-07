package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import java.util.Date;
import java.util.Map;

public interface IStatisticsService {
    Map<Module, TimeDuration> getTimeDurationForModulesToday(Iterable<Module> modules);
    Map<Module, TimeDuration> getTimeDurationForLecturerModulesToday(Lecturer lecturer);
    TimeDuration getTimeDurationForModuleToday(Module module) throws StudentrackObjectNotFoundException;
    Map<Module, TimeDuration> getTimeDurationForModules(Iterable<Module> modules);
    Map<Module, TimeDuration> getTimeDurationForLecturerModules(Lecturer lecturer);
    TimeDuration getTimeDurationForModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForDateAndStudent(Date date, Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForTimeRangeAndStudent(Date start, Date end, Student student) throws StudentrackObjectNotFoundException;
    TimeDuration calculateTimeDuration(Iterable<TimeOrder> timeOrders);
    TimeDuration getTimeInvestDurationForTodayAndStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentWeekAndStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentMonthAndStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForDateAndModule(Date date, Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentYearAndStudent(Student student) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForTimeRangeAndModule(Date start, Date end, Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForTodayAndModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTotalTimeInvestDurationForModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentWeekAndModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentMonthAndModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTimeInvestDurationForCurrentYearAndModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getTotalTimeInvestDurationForToday() throws StudentrackObjectNotFoundException;
    TimeDuration getTotalTimeInvestDurationForTimeRange(Date start, Date end) throws StudentrackObjectNotFoundException;
    TimeDuration getTotalTimeInvestDurationForCurrentWeek() throws StudentrackObjectNotFoundException;
    Grade getAverageGradeForModule(Module module) throws StudentrackObjectNotFoundException;
    Grade getAverageGradeForModuleWithoutFailedGrades(Module module) throws StudentrackObjectNotFoundException;
    double calculateGradeValue(Iterable<ModuleResults> moduleResults, boolean calculateWithFailedGrade);
    double getFailureRateForModule(Module module) throws StudentrackObjectNotFoundException;
    TimeDuration getAverageTimeInvestDurationForModule(Module module) throws StudentrackObjectNotFoundException;
    Grade getAverageGradeForStudent(Student student) throws StudentrackObjectNotFoundException;
    Map<Student, Grade> getAverageGradeForStudents(Iterable<Student> students);
    ModuleStatisticsShell buildModuleStatisticsShell(Module module);
    Iterable<ModuleStatisticsShell> getModuleStatisticsShellsForLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException;
    Iterable<ModuleTimeStatisticsShell> getModuleTimeStatisticShellsForLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException;
    ModuleTimeStatisticsShell buildModuleTimeStatisticsShell(Module module);
    ModuleTimeStatisticsShell buildModuleTimeStatisticsShellOverviewForLecturer(Lecturer lecturer) throws StudentrackObjectNotFoundException;
    Iterable<ModuleStatisticsShell> getModuleStatisticsShellForAllModules();
    Iterable<ModuleTimeStatisticsShell> getModuleTimeStatisticsForAllModules();
    ModuleTimeStatisticsShell getModuleTimeStatisticsOverviewForAllModules();
    ModuleTimeStatisticsShell buildModuleTimeStatisticsShellOverviewForModules(Iterable<Module> modules);
    Date[] getWeekStartAndEnd();
    Date[] getMonthStartAndEnd();
    Date[] getYearStartAndEnd();
    Date prepareInputDate(Date date, boolean back);
    Date setTimeToMidnight(Date date);
}
