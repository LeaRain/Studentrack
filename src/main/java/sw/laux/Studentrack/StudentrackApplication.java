package sw.laux.Studentrack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
public class StudentrackApplication {
	private static final Logger logger = LogManager.getLogger(StudentrackApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(StudentrackApplication.class, args);
	}
	/*
	@GetMapping("/write")
	public String write (@RequestParam(value="personid") long userId, @RequestParam(value="name") String name) {
		User newUser = new User(userId, name);
		repo.save(newUser);
		return newUser.toString();
	}

	@GetMapping("/read")
	public String read (@RequestParam(value="personid") long userId) {
		Optional<User> userOptional = repo.findById(userId);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			return user.toString();
		}

		return null;
	}

	 */
}
