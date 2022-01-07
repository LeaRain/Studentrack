package sw.laux.Studentrack.persistence.entities;

public class ModuleTimeStatisticsShell {
    private Module module;
    private TimeDuration timeInvestDurationToday;
    private TimeDuration timeInvestDurationWeek;
    private TimeDuration timeInvestDurationMonth;
    private TimeDuration timeInvestDurationYear;
    private TimeDuration timeInvestDurationTotal;

    public ModuleTimeStatisticsShell() {}

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public TimeDuration getTimeInvestDurationToday() {
        return timeInvestDurationToday;
    }

    public void setTimeInvestDurationToday(TimeDuration timeInvestDurationToday) {
        this.timeInvestDurationToday = timeInvestDurationToday;
    }

    public TimeDuration getTimeInvestDurationWeek() {
        return timeInvestDurationWeek;
    }

    public void setTimeInvestDurationWeek(TimeDuration timeInvestDurationWeek) {
        this.timeInvestDurationWeek = timeInvestDurationWeek;
    }

    public TimeDuration getTimeInvestDurationMonth() {
        return timeInvestDurationMonth;
    }

    public void setTimeInvestDurationMonth(TimeDuration timeInvestDurationMonth) {
        this.timeInvestDurationMonth = timeInvestDurationMonth;
    }

    public TimeDuration getTimeInvestDurationYear() {
        return timeInvestDurationYear;
    }

    public void setTimeInvestDurationYear(TimeDuration timeInvestDurationYear) {
        this.timeInvestDurationYear = timeInvestDurationYear;
    }

    public TimeDuration getTimeInvestDurationTotal() {
        return timeInvestDurationTotal;
    }

    public void setTimeInvestDurationTotal(TimeDuration timeInvestDurationTotal) {
        this.timeInvestDurationTotal = timeInvestDurationTotal;
    }
}
