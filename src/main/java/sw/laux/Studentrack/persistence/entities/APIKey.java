package sw.laux.Studentrack.persistence.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class APIKey extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long keyId;
    @OneToOne
    private Developer developer;
    @Column(name="key_string")
    private String key;
    private Date expirationDate;

    public APIKey() {

    }

    @Override
    public Long getId() {
        return getKeyId();
    }

    public long getKeyId() {
        return keyId;
    }

    public void setKeyId(long keyId) {
        this.keyId = keyId;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
