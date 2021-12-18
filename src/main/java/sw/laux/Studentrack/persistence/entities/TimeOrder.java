package sw.laux.Studentrack.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class TimeOrder extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long timeOrderId;
    private Timestamp start;
    // Bypass reserved keyword "end"
    @Column(name="end_time")
    private Timestamp end;
    @ManyToOne
    private Module module;
    @ManyToOne
    private Student owner;
    @OneToOne
    private TimeInvest timeInvest;

    public Student getOwner() {
        return owner;
    }

    public void setOwner(Student owner) {
        this.owner = owner;
    }

    public TimeInvest getTimeInvest() {
        return timeInvest;
    }

    public void setTimeInvest(TimeInvest timeInvest) {
        this.timeInvest = timeInvest;
    }

    public TimeOrder(Timestamp start, Timestamp end, Module module, Student owner, TimeInvest timeInvest) {
        this.start = start;
        this.end = end;
        this.module = module;
        this.owner = owner;
        this.timeInvest = timeInvest;
    }

    public long getTimeOrderId() {
        return timeOrderId;
    }

    public void setTimeOrderId(long timeOrderId) {
        this.timeOrderId = timeOrderId;
    }

    public TimeOrder() {

    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    @Override
    public Long getId() {
        return getTimeOrderId();
    }


    @Override
    public String toString() {
        return "TimeOrder{" +
                "timeOrderId=" + timeOrderId +
                ", start=" + start +
                ", end=" + end +
                ", owner=" + owner +
                ", timeInvest=" + timeInvest +
                '}';
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
