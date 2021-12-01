package sw.laux.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Objects;

@Entity
public abstract class User extends SingleIdEntity<Long>{
    @Id
    @GeneratedValue
    private long userId;
    private String firstName;
    private String lastName;
    private String password;
    private String mailAddress;
    private Date membershipStart;
    @ManyToOne
    private Faculty faculty;


    public User() {
    }

    public User(String firstName, String lastName, String password, String mailAddress, Date membershipStart) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.mailAddress = mailAddress;
        this.membershipStart = membershipStart;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(firstName, user.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public Date getMembershipStart() {
        return membershipStart;
    }

    public void setMembershipStart(Date membershipStart) {
        this.membershipStart = membershipStart;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", mailAddress='" + mailAddress + '\'' +
                ", membershipStart=" + membershipStart +
                '}';
    }
}
