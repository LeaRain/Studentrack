package com.example.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Grade {
    @Id
    @GeneratedValue
    private long gradeId;
    double value;
    String description;
    int tryNumber;

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

    public Grade(double value, String description, int tryNumber) {
        this.value = value;
        this.description = description;
        this.tryNumber = tryNumber;
    }

    public Grade() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade = (Grade) o;
        return gradeId == grade.gradeId && Double.compare(grade.value, value) == 0 && tryNumber == grade.tryNumber && Objects.equals(description, grade.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradeId, value, description, tryNumber);
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeId=" + gradeId +
                ", value=" + value +
                ", description='" + description + '\'' +
                ", tryNumber=" + tryNumber +
                '}';
    }
}
