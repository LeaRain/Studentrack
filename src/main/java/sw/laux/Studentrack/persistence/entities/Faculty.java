package sw.laux.Studentrack.persistence.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Faculty extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long facultyId;
    private String name;
    @OneToMany(mappedBy="faculty")
    Collection<Major> majors;
    @OneToOne
    private Lecturer dean;
    @OneToMany(mappedBy="faculty")
    private Collection<User> members;

    public Collection<Major> getMajors() {
        return majors;
    }

    public void setMajors(Collection<Major> majors) {
        this.majors = majors;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "facultyId=" + facultyId +
                ", name='" + name + '\'' +
                ", majors=" + majors +
                ", dean=" + dean +
                '}';
    }

    public Lecturer getDean() {
        return dean;
    }

    public void setDean(Lecturer dean) {
        this.dean = dean;
    }

    public Faculty(String name, Collection<Major> majors, Lecturer dean) {
        this.name = name;
        this.majors = majors;
        this.dean = dean;
    }

    public Faculty() {

    }


    @Override
    public Long getId() {
        return getFacultyId();
    }

    public long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(long facultyId) {
        this.facultyId = facultyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
