package sw.laux.Studentrack.persistence.entities;

import java.util.ArrayList;
import java.util.List;

// Inspiration: https://www.baeldung.com/thymeleaf-list
public class ModuleResultsDTO {
    private List<ModuleResults> moduleResults;

    public ModuleResultsDTO() {
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
