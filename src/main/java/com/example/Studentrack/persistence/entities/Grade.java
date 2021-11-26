package com.example.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Grade {
    public Grade(double value, String description, int tryNumber, Module module, Student student) {
        this.value = value;
        this.description = description;
        this.tryNumber = tryNumber;
        this.module = module;
        this.student = student;
    }

    @Id
    @GeneratedValue
    private long gradeId;
    double value;
    String description;
    int tryNumber;
    @ManyToOne
    private Module module;
    @ManyToOne
    private Student student;


    public long getGradeId() {
        return gradeId;
    }

    public void setGradeId(long gradeId) {
        this.gradeId = gradeId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTryNumber() {
        return tryNumber;
    }

    public void setTryNumber(int tryNumber) {
        this.tryNumber = tryNumber;
    }

    public Grade() {

    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeId=" + gradeId +
                ", value=" + value +
                ", description='" + description + '\'' +
                ", tryNumber=" + tryNumber +
                ", module=" + module +
                ", student=" + student +
                '}';
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
