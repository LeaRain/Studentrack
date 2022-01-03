package sw.laux.Studentrack.persistence.entities;

import javax.persistence.*;

@Entity
public class Grade {
    @Id
    @GeneratedValue
    private long gradeId;
    private double value;
    private String description;
    private int tryNumber;
    @OneToOne(mappedBy="grade")
    private ModuleResults moduleResults;

    public Grade(double value, String description, int tryNumber, ModuleResults moduleResults) {
        this.value = value;
        this.description = description;
        this.tryNumber = tryNumber;
        this.moduleResults = moduleResults;
    }

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

        if (value <= 1.5) {
            setDescription("Very Good");
        } else if (value <= 2.5) {
            setDescription("Good");
        } else if (value <= 3.5) {
            setDescription("Satisfactory");
        } else if (value <= 4) {
            setDescription("Sufficient");
        } else {
            setDescription("Failed");
        }
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
        return value + " (" + tryNumber + ")";
    }

    public ModuleResults getModuleResults() {
        return moduleResults;
    }

    public void setModuleResults(ModuleResults moduleResults) {
        this.moduleResults = moduleResults;
    }

}
