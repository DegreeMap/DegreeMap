package com.degreemap.DegreeMap.courses;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.degreemap.DegreeMap.config.JpaTestConfig;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogRepository;
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;

@DataJpaTest
@Import(JpaTestConfig.class)
public class CourseRepositoryTests {

    // TODO review tests later

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseCatalogRepository courseCatalogRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void whenSaveCourse_thenSuccess() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);

        Course course = new Course(catalog, "Data Structures", "CS101", 4, "MIT", "Engineering", "Computer Science");
        Course savedCourse = courseRepository.save(course);
        CourseCatalog savedCatalog = courseCatalogRepository.save(course.getCourseCatalog());
        assertEquals(entityManager.find(Course.class, savedCourse.getId()), course);
        assertEquals(savedCatalog.getCourses().size(), 1);
        assertEquals(savedCatalog.getCourses().contains(savedCourse), true);
    }

    @Test
    void whenSaveCourseWithNullName_thenThrowException() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            Course course = new Course(catalog, "", "CS101", 4, "MIT", "Engineering", "Computer Science");
            courseRepository.save(course);
        });

        assertTrue(exception.getMessage().equals("Name cannot be null or blank"));
    }

    @Test
    void whenSaveCourseWithNullCatalog_thenThrowException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            Course course = new Course(null, "hello", "CS101", 4, "MIT", "Engineering", "Computer Science");
            courseRepository.save(course);
        });
        assertTrue(exception.getMessage().equals("Course must be tied to a CourseCatalog"));
    }

    @Test
    void whenFindById_thenCorrectCourse() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);

        Course course = new Course(catalog, "Algorithms", "CS102", 3, "MIT", "Engineering", "Computer Science");
        entityManager.persist(course);

        Course found = courseRepository.findById(course.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Algorithms", found.getName());
    }

    @Test
    void whenDeleteCourse_thenRemoved() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);

        Course course = new Course(catalog, "Machine Learning", "CS103", 3, "MIT", "Engineering", "Computer Science");
        Course savedCourse = entityManager.persistAndFlush(course);

        courseRepository.delete(savedCourse);
        assertNull(entityManager.find(Course.class, savedCourse.getId()));
    }

    @Test
    void whenDeleteCatalog_thenCourseRemoved() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persistAndFlush(catalog);

        Course course = new Course(catalog, "Machine Learning", "CS103", 3, "MIT", "Engineering", "Computer Science");
        Course savedCourse = entityManager.persistAndFlush(course);

        courseCatalogRepository.delete(catalog);
        assertNull(entityManager.find(Course.class, savedCourse.getId()));
    }
}

