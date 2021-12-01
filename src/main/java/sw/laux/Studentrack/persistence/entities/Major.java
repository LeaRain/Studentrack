package sw.laux.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Major {
    @Id
    @GeneratedValue
    private long majorId;
    private String academicTitle;
    int studentNumber;
    @ManyToOne
    private Faculty faculty;

    public Major(String academicTitle, int studentNumber) {
        this.academicTitle = academicTitle;
        this.studentNumber = studentNumber;
    }

    public Major() {

    }

    public long getMajorId() {
        return majorId;
    }

    public void setMajorId(long majorId) {
        this.majorId = majorId;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Major major = (Major) o;
        return majorId == major.majorId && studentNumber == major.studentNumber && Objects.equals(academicTitle, major.academicTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorId, academicTitle, studentNumber);
    }
}
