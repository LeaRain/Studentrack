package com.example.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Course extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long courseId;
    private Date startDate;
    private Date endDate;
    @ManyToOne
    private Module module;

    public Course(Date startDate, Date endDate, Module module) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.module = module;
    }

    public Course() {

    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
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

    @Override
    public Long getId() {
        return getCourseId();
    }
}
