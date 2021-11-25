package com.example.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TimeInvest extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long timeInvestId;
    private long duration;

    public TimeInvest(long duration) {
        this.duration = duration;
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

    public TimeInvest() {

    }
    //private Course course;

    @Override
    public Long getId() {
        return null;
    }
}
