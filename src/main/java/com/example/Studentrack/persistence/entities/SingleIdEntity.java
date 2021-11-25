package com.example.Studentrack.persistence.entities;

import java.io.Serializable;

public abstract class SingleIdEntity<U extends Comparable> implements Serializable, Comparable<SingleIdEntity> {
    public abstract U getId();

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        SingleIdEntity<U> otherSingleIdObject = (SingleIdEntity<U>) obj;

        return getId() != null ? getId().equals(otherSingleIdObject.getId()) : otherSingleIdObject.getId() == null;
    }

    @Override
    public int compareTo(SingleIdEntity other) {
        return this.getId().compareTo(other.getId());
    }

}
