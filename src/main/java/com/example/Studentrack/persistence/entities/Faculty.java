package com.example.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Faculty extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long facultyId;
    private String name;

    public Faculty(String name) {
        this.name = name;
    }


    @Override
    public Long getId() {
        return getFacultyId();
    }

    public long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(long facultyId) {
        this.facultyId = facultyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
