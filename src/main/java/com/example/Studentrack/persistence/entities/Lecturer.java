package com.example.Studentrack.persistence.entities;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Lecturer extends User {
    private String title;

    public Lecturer(String firstName, String lastName, String password, String mailAddress, Date membershipStart, String title) {
        super(firstName, lastName, password, mailAddress, membershipStart);
        this.title = title;
    }

    public Lecturer() {
    }

    @Override
    public Long getId() {
        return getUserId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
