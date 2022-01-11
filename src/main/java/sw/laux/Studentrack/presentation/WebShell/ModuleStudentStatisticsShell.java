package sw.laux.Studentrack.presentation.WebShell;

import sw.laux.Studentrack.persistence.entities.Grade;
import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.TimeDuration;

public class ModuleStudentStatisticsShell {
    private Module module;
    private Grade studentGrade;
    private Grade averageGrade;
    private TimeDuration studentTimeInvestDuration;
    private TimeDuration averageTimeInvestDuration;

    public ModuleStudentStatisticsShell() {

    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Grade getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(Grade studentGrade) {
        this.studentGrade = studentGrade;
    }

    public Grade getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Grade averageGrade) {
        this.averageGrade = averageGrade;
    }

    public TimeDuration getStudentTimeInvestDuration() {
        return studentTimeInvestDuration;
    }

    public void setStudentTimeInvestDuration(TimeDuration studentTimeInvestDuration) {
        this.studentTimeInvestDuration = studentTimeInvestDuration;
    }

    public TimeDuration getAverageTimeInvestDuration() {
        return averageTimeInvestDuration;
    }

    public void setAverageTimeInvestDuration(TimeDuration averageTimeInvestDuration) {
        this.averageTimeInvestDuration = averageTimeInvestDuration;
    }
}
