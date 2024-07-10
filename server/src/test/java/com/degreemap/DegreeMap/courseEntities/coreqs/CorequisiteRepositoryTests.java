package com.degreemap.DegreeMap.courseEntities.coreqs;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.degreemap.DegreeMap.config.JpaTestConfig;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.corequisites.Corequisite;
import com.degreemap.DegreeMap.courseEntities.corequisites.CorequisiteRepository;
import com.degreemap.DegreeMap.courseEntities.courses.Course;

@DataJpaTest
@Import(JpaTestConfig.class)
public class CorequisiteRepositoryTests {

    @Autowired
    private CorequisiteRepository corequisiteRepository;

    // @Autowired
    // private CourseRepository courseRepository;
    // not needed (thought i did at first)

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void whenSaveCorequisite_thenSuccess() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);
        Course coreqCourse = new Course(catalog, "Intro to CS", "CS100", 3, "MIT", "Engineering", "Computer Science");
        Course connectedCourse = new Course(catalog, "Data Structures", "CS101", 4, "MIT", "Engineering", "Computer Science");
        entityManager.persist(coreqCourse);
        entityManager.persist(connectedCourse);

        Corequisite corequisite = new Corequisite(coreqCourse, connectedCourse);
        Corequisite savedCorequisite = corequisiteRepository.save(corequisite);

        assertNotNull(savedCorequisite.getId());
        assertEquals(corequisite.getCoreqCourse(), coreqCourse);
        assertEquals(corequisite.getConnectedCourse(), connectedCourse);
    }

    @Test
    void whenSaveReciprocal_searchForReciprocal_thenSuccess() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);
        Course coreqCourse = new Course(catalog, "Intro to CS", "CS100", 3, "MIT", "Engineering", "Computer Science");
        Course connectedCourse = new Course(catalog, "Data Structures", "CS101", 4, "MIT", "Engineering", "Computer Science");
        entityManager.persist(coreqCourse);
        entityManager.persist(connectedCourse);

        Corequisite corequisite = new Corequisite(coreqCourse, connectedCourse);
        Corequisite reciprocal = new Corequisite(connectedCourse, coreqCourse);
        Corequisite savedCorequisite = corequisiteRepository.save(corequisite);
        Corequisite savedReciprocal = corequisiteRepository.save(reciprocal);

        assertNotNull(savedCorequisite.getId());
        assertEquals(corequisite.getCoreqCourse(), coreqCourse);
        assertEquals(corequisite.getConnectedCourse(), connectedCourse);
        assertNotNull(savedReciprocal.getId());
        assertEquals(reciprocal.getCoreqCourse(), connectedCourse);
        assertEquals(reciprocal.getConnectedCourse(), coreqCourse);

        Corequisite foundReciprocal1 = corequisiteRepository.findReciprocal(coreqCourse, connectedCourse).get();
        assertEquals(reciprocal, foundReciprocal1);
        Corequisite foundReciprocal2 = corequisiteRepository.findReciprocal(connectedCourse, coreqCourse).get();
        assertEquals(corequisite, foundReciprocal2);
    }

    @Test
    void whenSavePrerequisiteWithNullFields_thenThrowException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            Corequisite corequisite = new Corequisite(null, null);
            corequisiteRepository.save(corequisite);
        });

        System.out.println("wovbwobwobwbv " + exception.getMessage());
        assertEquals(exception.getMessage(), "All fields must be filled for Corequisites");
    }

    @Test
    void whenDeletePrerequisite_thenRemoved() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);
        Course coreqCourse = new Course(catalog, "Intro to Algorithms", "CS150", 3, "MIT", "Engineering", "Computer Science");
        Course connectedCourse = new Course(catalog, "Advanced Algorithms", "CS250", 4, "MIT", "Engineering", "Computer Science");
        entityManager.persist(coreqCourse);
        entityManager.persist(connectedCourse);

        Corequisite corequisite = new Corequisite(coreqCourse, connectedCourse);
        Corequisite savedCorequisite = entityManager.persistAndFlush(corequisite);

        corequisiteRepository.delete(savedCorequisite);
        assertEquals(entityManager.find(Course.class, coreqCourse.getId()), coreqCourse);
        assertEquals(entityManager.find(Course.class, connectedCourse.getId()), connectedCourse);
        assertNull(entityManager.find(Corequisite.class, savedCorequisite.getId()));
        assertEquals(entityManager.find(Course.class, coreqCourse.getId()), coreqCourse);
        assertEquals(entityManager.find(Course.class, connectedCourse.getId()), connectedCourse);
    }
}
