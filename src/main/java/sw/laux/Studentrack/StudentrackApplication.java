package sw.laux.Studentrack;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class StudentrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentrackApplication.class, args);
	}
}
