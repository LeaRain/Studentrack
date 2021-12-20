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
    private Collection<Grade> resultGrades;
    @ManyToMany(mappedBy="modules")
    private Collection<Student> students = new ArrayList<Student>();
    @ElementCollection
    private Collection<Course> availableCourses;

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

    public Collection<Student> getStudents() {
        return Collections.unmodifiableCollection(students);
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
        }
    }

    public void removeStudent(Student student) {
        // If student is not in the collection, the remove method will return false and will not produce an exception.
        students.remove(student);
    }
}
