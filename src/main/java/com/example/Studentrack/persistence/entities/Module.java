package com.example.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Module extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long moduleId;
    private String name;
    private int ects;
    private int creditHours;
    private String description;

    public Module(String name, int ects, int creditHours, String description) {
        this.name = name;
        this.ects = ects;
        this.creditHours = creditHours;
        this.description = description;
    }

    public Module() {

    }

    @Override
    public Long getId() {
        return getModuleId();
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Module{" +
                "moduleId=" + moduleId +
                ", name='" + name + '\'' +
                ", ects=" + ects +
                ", creditHours=" + creditHours +
                ", description='" + description + '\'' +
                '}';
    }
}
