package sw.laux.Studentrack.persistence.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Period;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
public class TimeOrder extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long timeOrderId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;
    // Bypass reserved keyword "end"
    @Column(name="end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date end;
    @ManyToOne
    private Module module;
    @ManyToOne
    private Student owner;
    @OneToOne
    private TimeInvest timeInvest;
    @Transient
    private TimeOrderDuration duration;

    public TimeOrderDuration getDuration() {
        // Calculate the latest value
        setDuration();
        return duration;
    }

    public void setDuration() {
        if (start != null && end != null) {
            var durationInMilliSeconds = end.getTime() - start.getTime();
            duration = new TimeOrderDuration();
            duration.setDuration(durationInMilliSeconds);
        }
    }

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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public Long getId() {
        return getTimeOrderId();
    }


    @Override
    public String toString() {
        return "TimeOrder:" +
                " start: " + start +
                " end: " + end;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
