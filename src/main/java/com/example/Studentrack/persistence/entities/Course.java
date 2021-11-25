package com.example.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Course extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long courseId;
    Date startDate;
    Date endDate;

    public Course(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Course() {

    }

    @Override
    public Long getId() {
        return null;
    }
}
