package sw.laux.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class TimeOrderWebShell extends TimeOrder{
    @OneToOne
    private TimeOrder timeOrder;
    private String startString;
    private String endString;

    public TimeOrderWebShell() {

    }

    public TimeOrder getTimeOrder() {
        return timeOrder;
    }

    public void setTimeOrder(TimeOrder timeOrder) {
        this.timeOrder = timeOrder;
    }

    public String getStartString() {
        return startString;
    }

    public void setStartString(String startString) {
        this.startString = startString;
    }

    public String getEndString() {
        return endString;
    }

    public void setEndString(String endString) {
        this.endString = endString;
    }
}
