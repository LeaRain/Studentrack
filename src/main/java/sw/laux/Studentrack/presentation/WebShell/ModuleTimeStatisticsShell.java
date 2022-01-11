package sw.laux.Studentrack.presentation.WebShell;

import sw.laux.Studentrack.persistence.entities.Module;

public class ModuleTimeStatisticsShell extends TimeStatisticsShell {
    private Module module;

    public ModuleTimeStatisticsShell() {}

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }


}
