package sw.laux.Studentrack.persistence.entities;

import javax.persistence.*;

@Entity
public class ModuleResults extends SingleIdEntity<Long> {
    @Id
    @GeneratedValue
    private long moduleResultsId;
    @ManyToOne
    private Module module;
    @ManyToOne
    private Student student;
    @OneToOne(cascade={CascadeType.ALL}, orphanRemoval=true)
    private TimeInvest timeInvest;
    @OneToOne(cascade={CascadeType.ALL}, orphanRemoval=true)
    private Grade grade;

    public ModuleResults(long moduleResultsId, Module module, Student student, TimeInvest timeInvest, Grade grade) {
        this.moduleResultsId = moduleResultsId;
        this.module = module;
        this.student = student;
        this.timeInvest = timeInvest;
        this.grade = grade;
    }

    public ModuleResults() {

    }

    @Override
    public Long getId() {
        return getModuleResultsId();
    }

    public long getModuleResultsId() {
        return moduleResultsId;
    }

    public void setModuleResultsId(long moduleResultsId) {
        this.moduleResultsId = moduleResultsId;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public TimeInvest getTimeInvest() {
        return timeInvest;
    }

    public void setTimeInvest(TimeInvest timeInvest) {
        this.timeInvest = timeInvest;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
