package com.degreemap.DegreeMap.courseEntities.prereqs;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.degreemap.DegreeMap.config.JpaTestConfig;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.prerequisites.GradeRequirement;
import com.degreemap.DegreeMap.courseEntities.prerequisites.Prerequisite;
import com.degreemap.DegreeMap.courseEntities.prerequisites.PrerequisiteRepository;

@DataJpaTest
@Import(JpaTestConfig.class)
public class PrerequisiteRepositoryTests {

    @Autowired
    private PrerequisiteRepository prerequisiteRepository;

    // @Autowired
    // private CourseRepository courseRepository;
    // not needed (thought i did at first)

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void whenSavePrerequisite_thenSuccess() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);
        Course prereqCourse = new Course(catalog, "Intro to CS", "CS100", 3, "MIT", "Engineering", "Computer Science");
        Course connectedCourse = new Course(catalog, "Data Structures", "CS101", 4, "MIT", "Engineering", "Computer Science");
        entityManager.persist(prereqCourse);
        entityManager.persist(connectedCourse);

        Prerequisite prerequisite = new Prerequisite(GradeRequirement.A, prereqCourse, connectedCourse);
        Prerequisite savedPrerequisite = prerequisiteRepository.save(prerequisite);

        assertNotNull(savedPrerequisite.getId());
        assertEquals(prerequisite.getPrereqCourse(), prereqCourse);
        assertEquals(prerequisite.getConnectedCourse(), connectedCourse);
    }

    @Test
    void whenSavePrerequisiteWithNullFields_thenThrowException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            Prerequisite prerequisite = new Prerequisite(null, null, null);
            prerequisiteRepository.save(prerequisite);
        });

        assertTrue(exception.getMessage().contains("All fields must be filled for Prerequisites"));
    }

    @Test
    void whenFindByConnectedCourse_thenCorrectPrerequisite() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);
        Course prereqCourse = new Course(catalog, "Intro to Programming", "CS100", 3, "MIT", "Engineering", "Computer Science");
        Course connectedCourse = new Course(catalog, "Object-Oriented Programming", "CS200", 3, "MIT", "Engineering", "Computer Science");
        entityManager.persist(prereqCourse);
        entityManager.persist(connectedCourse);

        Prerequisite prerequisite = new Prerequisite(GradeRequirement.A, prereqCourse, connectedCourse);
        entityManager.persist(prerequisite);

        Prerequisite found = prerequisiteRepository.findByConnectedCourseId(connectedCourse.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getPrereqCourse(), prereqCourse);
    }

    @Test
    void whenDeletePrerequisite_thenRemoved() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);
        Course prereqCourse = new Course(catalog, "Intro to Algorithms", "CS150", 3, "MIT", "Engineering", "Computer Science");
        Course connectedCourse = new Course(catalog, "Advanced Algorithms", "CS250", 4, "MIT", "Engineering", "Computer Science");
        entityManager.persist(prereqCourse);
        entityManager.persist(connectedCourse);

        Prerequisite prerequisite = new Prerequisite(GradeRequirement.A, prereqCourse, connectedCourse);
        Prerequisite savedPrerequisite = entityManager.persistAndFlush(prerequisite);

        prerequisiteRepository.delete(savedPrerequisite);
        assertNull(entityManager.find(Prerequisite.class, savedPrerequisite.getId()));
    }
}
