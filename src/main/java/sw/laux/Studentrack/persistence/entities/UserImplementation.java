package sw.laux.Studentrack.persistence.entities;

public class UserImplementation extends User {
    @Override
    public Long getId() {
        return getUserId();
    }
}
