package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.StudentrackObjectNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.IModuleService;
import sw.laux.Studentrack.application.services.interfaces.IStatisticsService;
import sw.laux.Studentrack.application.services.interfaces.ITimeService;
import sw.laux.Studentrack.persistence.entities.*;
import sw.laux.Studentrack.persistence.entities.Module;

import java.util.*;

@Service
public class StatisticService implements IStatisticsService {
    @Autowired
    IModuleService moduleService;
    @Autowired
    private ITimeService timeService;

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
    public Map<Module, TimeDuration> getTimeDurationForLecturerModulesToday(Lecturer lecturer) {
        return getTimeDurationForModulesToday(lecturer.getModules());
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
