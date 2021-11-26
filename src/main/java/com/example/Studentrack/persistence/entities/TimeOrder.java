package com.example.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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

    public TimeOrder(Timestamp start, Timestamp end, Course course) {
        this.start = start;
        this.end = end;
        this.course = course;
    }

    public long getTimeOrderId() {
        return timeOrderId;
    }

    @Override
    public String toString() {
        return "TimeOrder{" +
                "timeOrderId=" + timeOrderId +
                ", start=" + start +
                ", end=" + end +
                ", course=" + course +
                '}';
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
}
