package sw.laux.Studentrack.application.services.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sw.laux.Studentrack.persistence.entities.Course;
import sw.laux.Studentrack.persistence.repository.CourseRepository;

@Service
public class CourseService implements ICourseService{
    @Autowired
    CourseRepository courseRepo;

    @Override
    public Course saveCourse(Course course) {
        return courseRepo.save(course);
    }
}
