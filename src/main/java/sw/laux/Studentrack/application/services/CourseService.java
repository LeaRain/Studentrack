package sw.laux.Studentrack.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.application.exceptions.CourseAlreadySavedException;
import sw.laux.Studentrack.application.exceptions.CourseNotFoundException;
import sw.laux.Studentrack.application.services.interfaces.ICourseService;
import sw.laux.Studentrack.persistence.entities.Course;
import sw.laux.Studentrack.persistence.repository.CourseRepository;

import java.util.Optional;

@Service
public class CourseService implements ICourseService {
    @Autowired
    CourseRepository courseRepo;

    @Override
    public Course saveCourse(Course course) {
        return courseRepo.save(course);
    }

    @Override
    public Course findCourse(Course course) throws CourseNotFoundException {
        return findCourse(course.getCourseId());
    }

    @Override
    public Course findCourse(long courseId) throws CourseNotFoundException {
        var courseOptional = courseRepo.findById(courseId);
        if (courseOptional.isEmpty()) {
            throw new CourseNotFoundException("Course with id " + courseId + " not found!");
        }

        return courseOptional.get();
    }

    @Override
    public Course updateCourse(Course course) throws CourseNotFoundException {
        // Throws exception, if course does not exist
        findCourse(course);
        // Automatically updates with repo method
        return saveCourse(course);
    }
}
