package sw.laux.Studentrack.persistence.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Entity
public class Lecturer extends User {
    private String title;
    @OneToOne(mappedBy="dean", cascade={CascadeType.DETACH})
    private Faculty deanOfFaculty;
    @OneToMany(mappedBy="responsibleLecturer", cascade={CascadeType.DETACH})
    private Collection<Module> modules;

    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection(modules);
    }

    public void setModules(Collection<Module> modules) {
        this.modules = modules;
    }

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

    @Override
    public String toString() {
        return title + " " + getFirstName() + " " + getLastName();
    }

    public Faculty getDeanOfFaculty() {
        return deanOfFaculty;
    }

    public void setDeanOfFaculty(Faculty deanOfFaculty) {
        this.deanOfFaculty = deanOfFaculty;
    }

    public void removeAllModules() {
        modules.clear();
    }
}
