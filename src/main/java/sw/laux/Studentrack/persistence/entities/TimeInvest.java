package sw.laux.Studentrack.persistence.entities;

import sw.laux.Studentrack.persistence.entities.Course;
import sw.laux.Studentrack.persistence.entities.SingleIdEntity;
import sw.laux.Studentrack.persistence.entities.TimeOrder;

import javax.persistence.*;

@Entity
public class TimeInvest extends SingleIdEntity<Long> {
    @Id
    @GeneratedValue
    private long timeInvestId;
    private long duration;
    @OneToOne
    private TimeOrder timeOrder;
    @ManyToOne
    private Module module;

    public TimeInvest(long duration, TimeOrder timeOrder, Module module) {
        this.duration = duration;
        this.timeOrder = timeOrder;
        this.module = module;
    }

    public TimeInvest() {

    }

    @Override
    public Long getId() {
        return getTimeInvestId();
    }

    public long getTimeInvestId() {
        return timeInvestId;
    }

    public void setTimeInvestId(long timeInvestId) {
        this.timeInvestId = timeInvestId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public TimeOrder getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(TimeOrder timeOrder) {
        this.timeOrder = timeOrder;
    }

    @Override
    public String toString() {
        return "TimeInvest{" +
                "timeInvestId=" + timeInvestId +
                ", duration=" + duration +
                ", timeOrder=" + timeOrder +
                '}';
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
