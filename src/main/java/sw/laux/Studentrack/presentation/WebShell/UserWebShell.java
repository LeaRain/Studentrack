package sw.laux.Studentrack.presentation.WebShell;

import sw.laux.Studentrack.persistence.entities.Lecturer;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.entities.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class UserWebShell extends User {
    private boolean isStudent;
    private String academicTitle;
    @OneToOne
    private Student student;
    @OneToOne
    private Lecturer lecturer;

    public UserWebShell() {

    }

    @Override
    public Long getId() {
        return getUserId();
    }

    // Usage of getIsStudent and setIsStudent for NotReadablePropertyException of Spring for invalid getter and setter.
    public boolean getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(boolean student) {
        isStudent = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }
}
