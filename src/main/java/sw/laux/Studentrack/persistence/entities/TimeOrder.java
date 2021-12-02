package sw.laux.Studentrack.persistence.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class TimeOrder extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long timeOrderId;
    private Timestamp start;
    private Timestamp end;
    @ManyToOne
    private Course course;
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

    public TimeOrder(Timestamp start, Timestamp end, Course course, Student owner, TimeInvest timeInvest) {
        this.start = start;
        this.end = end;
        this.course = course;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "TimeOrder{" +
                "timeOrderId=" + timeOrderId +
                ", start=" + start +
                ", end=" + end +
                ", course=" + course +
                ", owner=" + owner +
                ", timeInvest=" + timeInvest +
                '}';
    }
}
