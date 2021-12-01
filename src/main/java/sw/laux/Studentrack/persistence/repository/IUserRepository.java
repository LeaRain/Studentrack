package sw.laux.Studentrack.persistence.repository;

import sw.laux.Studentrack.persistence.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends CrudRepository<User, Long> {
}

