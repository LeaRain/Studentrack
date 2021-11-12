package com.example.Studentrack;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Person {
    @Id
    private long personId;
    private String name;

    public Person() {
        personId = 0;
        name = null;
    }

    public Person(long personId, String name) {
        this.personId = personId;
        this.name = name;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long userId) {
        this.personId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return personId == person.personId && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personId, name);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + personId +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
