package sw.laux.Studentrack.persistence.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sw.laux.Studentrack.security.Authority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
// Bypass reserved keyword "user"
@Table(name="users")
public abstract class User extends SingleIdEntity<Long> implements UserDetails {
    @Id
    @GeneratedValue
    private long userId;
    private String firstName;
    private String lastName;
    private String password;
    @Column(unique=true)
    private String mailAddress;
    private Date membershipStart;
    @ManyToOne
    private Faculty faculty;


    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public User() {
    }

    public User(String firstName, String lastName, String password, String mailAddress, Date membershipStart, Faculty faculty) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.mailAddress = mailAddress;
        this.membershipStart = membershipStart;
        this.faculty = faculty;
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
    public Long getId() {
        return null;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new Authority(this.getClass().getSimpleName()));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return mailAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
        return firstName + " " + lastName + " (" + mailAddress + ")";
    }
}
