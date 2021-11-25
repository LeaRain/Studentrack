package com.example.Studentrack.persistence.entities;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Student extends User {
    private int ects;
    boolean isPremiumUser;

    public Student(int ects, boolean isPremiumUser) {
        this.ects = ects;
        this.isPremiumUser = isPremiumUser;
    }

    public Student(String firstName, String lastName, String password, String mailAddress, Date membershipStart, int ects, boolean isPremiumUser) {
        super(firstName, lastName, password, mailAddress, membershipStart);
        this.ects = ects;
        this.isPremiumUser = isPremiumUser;
    }

    public Student() {

    }


    @Override
    public Long getId() {
        return getUserId();
    }

    public int getEcts() {
        return ects;
    }

    public boolean isPremiumUser() {
        return isPremiumUser;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public void setPremiumUser(boolean premiumUser) {
        isPremiumUser = premiumUser;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ects=" + ects +
                ", isPremiumUser=" + isPremiumUser +
                '}';
    }
}
