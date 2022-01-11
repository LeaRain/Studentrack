package sw.laux.Studentrack.presentation.WebShell;

import sw.laux.Studentrack.persistence.entities.ModuleResults;

import java.util.ArrayList;
import java.util.List;

// Inspiration: https://www.baeldung.com/thymeleaf-list
public class ModuleResultsListShell {
    private List<ModuleResults> moduleResults;

    public ModuleResultsListShell() {
        moduleResults = new ArrayList<ModuleResults>();
    }

    public List<ModuleResults> getModuleResults() {
        return moduleResults;
    }

    public void setModuleResults(List<ModuleResults> moduleResults) {
        this.moduleResults = moduleResults;
    }

    public void addModuleResult(ModuleResults moduleResult) {
        moduleResults.add(moduleResult);
    }
}
