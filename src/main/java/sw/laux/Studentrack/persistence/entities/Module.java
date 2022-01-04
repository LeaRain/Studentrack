package sw.laux.Studentrack.persistence.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class Module extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long moduleId;
    @Column(unique=true)
    private String name;
    private int ects;
    private int creditHours;
    private String description;
    @ManyToOne
    private Lecturer responsibleLecturer;
    @OneToMany(mappedBy="module")
    private Collection<ModuleResults> moduleResults;
    @ManyToMany(mappedBy="modules")
    private Collection<Student> students;

    @Override
    public String toString() {
        return name;
    }

    public Lecturer getResponsibleLecturer() {
        return responsibleLecturer;
    }

    public void setResponsibleLecturer(Lecturer responsibleLecturer) {
        this.responsibleLecturer = responsibleLecturer;
    }

    public Module(String name, int ects, int creditHours, String description, Lecturer responsibleLecturer) {
        this.name = name;
        this.ects = ects;
        this.creditHours = creditHours;
        this.description = description;
        this.responsibleLecturer = responsibleLecturer;
    }

    public Module() {

    }

    @Override
    public Long getId() {
        return getModuleId();
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Student> getStudents() {
        return Collections.unmodifiableCollection(students);
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }

    public Collection<ModuleResults> getModuleResults() {
        return moduleResults;
    }

    public void setModuleResults(Collection<ModuleResults> moduleResults) {
        this.moduleResults = moduleResults;
    }

    public void addModuleResults(ModuleResults moduleResults) {
        if (!this.moduleResults.contains(moduleResults)) {
            this.moduleResults.add(moduleResults);
        }
    }

    public void removeModuleResults(ModuleResults moduleResults) {
        this.moduleResults.remove(moduleResults);
    }
}
