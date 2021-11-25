package com.example.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class TimeOrder extends SingleIdEntity<Long>{
    public long getTimeOrderId() {
        return timeOrderId;
    }

    public void setTimeOrderId(long timeOrderId) {
        this.timeOrderId = timeOrderId;
    }

    @Id
    @GeneratedValue
    private long timeOrderId;
    private Timestamp start;
    private Timestamp end;

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

    public TimeOrder(Timestamp start, Timestamp end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public Long getId() {
        return getTimeOrderId();
    }
}
