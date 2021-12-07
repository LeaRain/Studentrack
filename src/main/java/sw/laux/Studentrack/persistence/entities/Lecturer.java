package sw.laux.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.Date;

@Entity
public class Lecturer extends User {
    private String title;
    @OneToOne(mappedBy="dean")
    private Faculty deanOfFaculty;
    @OneToMany(mappedBy="responsibleLecturer")
    private Collection<Module> modules;

    public Lecturer(String firstName, String lastName, String password, String mailAddress, Date membershipStart, String title, Faculty faculty) {
        super(firstName, lastName, password, mailAddress, membershipStart, faculty);
        this.title = title;
    }

    public Lecturer() {
    }

    @Override
    public Long getId() {
        return getUserId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
