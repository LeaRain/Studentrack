package sw.laux.Studentrack.persistence.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Entity
public class Student extends User {
    private int ects;
    private boolean isPremiumUser;
    @OneToMany(mappedBy="student")
    private Collection<ModuleResults> moduleResults;
    @OneToMany(mappedBy="owner")
    private Collection<TimeOrder> timeOrders;
    @ManyToOne
    private Major major;
    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.DETACH})
    private Collection<Module> modules;

    public Collection<Module> getModules() {
        return modules;
    }

    public void setModules(Collection<Module> modules) {
        this.modules = modules;
    }

    public Student(String firstName, String lastName, String password, String mailAddress, Date membershipStart, int ects, boolean isPremiumUser, Faculty faculty, Major major) {
        super(firstName, lastName, password, mailAddress, membershipStart, faculty);
        this.ects = ects;
        this.isPremiumUser = isPremiumUser;
        this.major = major;
    }

    public Student() {

    }


    @Override
    public Long getId() {
        return getUserId();
    }

    public int getEcts() {
        return ects;
    }

    public boolean isPremiumUser() {
        return isPremiumUser;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public void setPremiumUser(boolean premiumUser) {
        isPremiumUser = premiumUser;
    }

    public Collection<TimeOrder> getTimeOrders() {
        return Collections.unmodifiableCollection(timeOrders);
    }

    public void setTimeOrders(Collection<TimeOrder> timeOrders) {
        this.timeOrders = timeOrders;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    public Collection<ModuleResults> getModuleResults() {
        return Collections.unmodifiableCollection(moduleResults);
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
