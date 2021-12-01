package sw.laux.Studentrack.persistence.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Module extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long moduleId;
    private String name;
    private int ects;
    private int creditHours;
    private String description;
    @ManyToOne
    private Lecturer responsibleLecturer;
    @OneToMany(mappedBy="module")
    private Collection<Grade> resultGrades;

    @Override
    public String toString() {
        return "Module{" +
                "moduleId=" + moduleId +
                ", name='" + name + '\'' +
                ", ects=" + ects +
                ", creditHours=" + creditHours +
                ", description='" + description + '\'' +
                ", responsibleLecturer=" + responsibleLecturer +
                ", resultGrades=" + resultGrades +
                ", availableCourses=" + availableCourses +
                '}';
    }

    public Lecturer getResponsibleLecturer() {
        return responsibleLecturer;
    }

    public void setResponsibleLecturer(Lecturer responsibleLecturer) {
        this.responsibleLecturer = responsibleLecturer;
    }

    public Collection<Grade> getResultGrades() {
        return resultGrades;
    }

    public void setResultGrades(Collection<Grade> resultGrades) {
        this.resultGrades = resultGrades;
    }

    public Collection<Course> getAvailableCourses() {
        return availableCourses;
    }

    public void setAvailableCourses(Collection<Course> availableCourses) {
        this.availableCourses = availableCourses;
    }

    @OneToMany(mappedBy="module")
    private Collection<Course> availableCourses;

    public Module(String name, int ects, int creditHours, String description, Lecturer responsibleLecturer, Collection<Grade> resultGrades, Collection<Course> availableCourses) {
        this.name = name;
        this.ects = ects;
        this.creditHours = creditHours;
        this.description = description;
        this.responsibleLecturer = responsibleLecturer;
        this.resultGrades = resultGrades;
        this.availableCourses = availableCourses;
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

}
