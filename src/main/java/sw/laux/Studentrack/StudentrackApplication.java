package sw.laux.Studentrack;

import sw.laux.Studentrack.persistence.entities.Student;
import sw.laux.Studentrack.persistence.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class StudentrackApplication {
	@Autowired
	private IUserRepository repo;

	public static void main(String[] args) {
		SpringApplication.run(StudentrackApplication.class, args);
		var student = new Student();
		System.out.println(student.getFirstName());
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
