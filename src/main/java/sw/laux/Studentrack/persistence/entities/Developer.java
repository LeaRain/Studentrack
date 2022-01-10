package sw.laux.Studentrack.persistence.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Developer extends User {
    private String organization;

    @OneToOne(cascade={CascadeType.ALL})
    private APIKey key;

    public Developer() {}

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public APIKey getKey() {
        return key;
    }

    public void setKey(APIKey key) {
        this.key = key;
    }

}
