package sw.laux.Studentrack.presentation.WebShell;

import sw.laux.Studentrack.persistence.entities.Module;
import sw.laux.Studentrack.persistence.entities.TimeDuration;

public class ModuleEstimationStatisticsShell {
    private Module module;
    private TimeDuration currentDuration;
    private TimeDuration estimatedDuration;
    private double currentPercentage;

    public ModuleEstimationStatisticsShell() {

    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public TimeDuration getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(TimeDuration currentDuration) {
        this.currentDuration = currentDuration;
    }

    public TimeDuration getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(TimeDuration estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public double getCurrentPercentage() {
        return currentPercentage;
    }

    public void setCurrentPercentage(double currentPercentage) {
        this.currentPercentage = currentPercentage;
    }
}
