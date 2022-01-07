package sw.laux.Studentrack.persistence.entities;

// Plain, simple object as container for statistics results in the frontend
public class ModuleStatisticsShell {
    private Module module;
    private TimeDuration totalTimeInvestDuration;
    private Grade averageGrade;
    private Grade averageGradeWithoutFailures;
    private double failureRate;
    private int numberOfStudents;

    public ModuleStatisticsShell() {};

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public TimeDuration getTotalTimeInvestDuration() {
        return totalTimeInvestDuration;
    }

    public void setTotalTimeInvestDuration(TimeDuration totalTimeInvestDuration) {
        this.totalTimeInvestDuration = totalTimeInvestDuration;
    }

    public Grade getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Grade averageGrade) {
        this.averageGrade = averageGrade;
    }

    public Grade getAverageGradeWithoutFailures() {
        return averageGradeWithoutFailures;
    }

    public void setAverageGradeWithoutFailures(Grade averageGradeWithoutFailures) {
        this.averageGradeWithoutFailures = averageGradeWithoutFailures;
    }

    public double getFailureRate() {
        return failureRate;
    }

    public void setFailureRate(double failureRate) {
        this.failureRate = failureRate;
    }
}
