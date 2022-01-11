package sw.laux.Studentrack.presentation.WebShell;

import sw.laux.Studentrack.persistence.entities.Grade;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.TimeDuration;

// Plain, simple object as container for statistics results in the frontend
public class ModuleStatisticsShell {
    private Module module;
    private TimeDuration totalTimeInvestDuration;
    private Grade averageGrade;
    private Grade averageGradeWithoutFailures;
    private double failureRate;
    private int numberOfStudents;
    private TimeDuration averageTimeInvestDuration;
    private TimeDuration estimatedTimeInvestDuration;

    public ModuleStatisticsShell() {};

    public TimeDuration getAverageTimeInvestDuration() {
        return averageTimeInvestDuration;
    }

    public void setAverageTimeInvestDuration(TimeDuration averageTimeInvestDuration) {
        this.averageTimeInvestDuration = averageTimeInvestDuration;
    }

    public TimeDuration getEstimatedTimeInvestDuration() {
        return estimatedTimeInvestDuration;
    }

    public void setEstimatedTimeInvestDuration(TimeDuration estimatedTimeInvestDuration) {
        this.estimatedTimeInvestDuration = estimatedTimeInvestDuration;
    }

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
