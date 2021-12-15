package sw.laux.Studentrack.application.services.interfaces;

import sw.laux.Studentrack.persistence.entities.Course;

public interface ICourseService {
    Course saveCourse(Course course);
}
