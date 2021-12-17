package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.application.exceptions.CourseNotFoundException;
import sw.laux.Studentrack.persistence.entities.Course;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ICourseService {
    Course saveCourse(Course course);
    Course findCourse(Course course) throws CourseNotFoundException;
    Course findCourse(long courseId) throws CourseNotFoundException;
    @Transactional
    Course updateCourse(Course course) throws CourseNotFoundException;
}
