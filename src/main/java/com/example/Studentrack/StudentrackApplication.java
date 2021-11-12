package com.example.Studentrack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@SpringBootApplication
@RestController
public class StudentrackApplication {
	@Autowired
	private UserRepository repo;

	public static void main(String[] args) {
		SpringApplication.run(StudentrackApplication.class, args);
	}

	@GetMapping("/write")
	public String write (@RequestParam(value="personid") long userId, @RequestParam(value="name") String name) {
		Person newPerson = new Person(userId, name);
		repo.save(newPerson);
		return newPerson.toString();
	}

	@GetMapping("/read")
	public String read (@RequestParam(value="personid") long userId) {
		Optional<Person> userOptional = repo.findById(userId);

		if (userOptional.isPresent()) {
			Person person = userOptional.get();
			return person.toString();
		}

		return null;
	}
}
