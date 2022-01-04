package sw.laux.Studentrack.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class UserPasswordChangeWebShell extends User{
    @OneToOne
    private User user;
    private String oldPassword;
    private String newPassword;

    public UserPasswordChangeWebShell() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
