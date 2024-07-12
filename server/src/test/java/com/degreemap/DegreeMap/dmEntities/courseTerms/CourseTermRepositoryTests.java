package com.degreemap.DegreeMap.dmEntities.courseTerms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.degreemap.DegreeMap.config.JpaTestConfig;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogRepository;
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.terms.Term;
import com.degreemap.DegreeMap.dmEntities.terms.TermRepository;
import com.degreemap.DegreeMap.dmEntities.years.Year;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import(JpaTestConfig.class)
public class CourseTermRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseTermRepository courseTermRepository;
    @Autowired
    private CourseCatalogRepository courseCatalogRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TermRepository termRepository;

    @Test
    public void shouldSaveCourseTerm() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);

        Course course = new Course(catalog, "Data Structures", "CS101", 4, "MIT", "Engineering", "Computer Science");
        Course savedCourse = courseRepository.save(course);
        CourseCatalog savedCatalog = courseCatalogRepository.save(course.getCourseCatalog());
        assertEquals(entityManager.find(Course.class, savedCourse.getId()), course);
        assertEquals(savedCatalog.getCourses().size(), 1);
        assertEquals(savedCatalog.getCourses().contains(savedCourse), true);
        
        entityManager.persist(course);

        DegreeMap dm = new DegreeMap("College");
        entityManager.persist(dm);
        Year year = new Year("2021-2022", dm); 
        entityManager.persist(year);
        Term term = new Term("Fall 2021", year);
        entityManager.persist(term);
        entityManager.flush();

        entityManager.persist(term);

        CourseTerm courseTerm = new CourseTerm(course, term);
        CourseTerm savedCourseTerm = courseTermRepository.save(courseTerm);
        
        assertThat(savedCourseTerm).isNotNull();
        assertThat(savedCourseTerm.getId()).isNotNull();
        assertThat(savedCourseTerm.getCourse()).isEqualTo(course);
        assertThat(savedCourseTerm.getTerm()).isEqualTo(term);
    }

    @Test
    public void shouldFindCourseTermById() {
        Course course = new Course();
        entityManager.persist(course);
        Term term = new Term();
        entityManager.persist(term);

        CourseTerm courseTerm = new CourseTerm(course, term);
        CourseTerm savedCourseTerm = entityManager.persistFlushFind(courseTerm);

        Optional<CourseTerm> foundCourseTerm = courseTermRepository.findById(savedCourseTerm.getId());
        assertThat(foundCourseTerm).isPresent();
        assertThat(foundCourseTerm.get().getCourse()).isEqualTo(course);
        assertThat(foundCourseTerm.get().getTerm()).isEqualTo(term);
    }

    @Test
    public void shouldDeleteCourseTerm() {
        Course course = new Course();
        entityManager.persist(course);
        Term term = new Term();
        entityManager.persist(term);

        CourseTerm courseTerm = new CourseTerm(course, term);
        CourseTerm savedCourseTerm = entityManager.persistFlushFind(courseTerm);
        courseTermRepository.delete(savedCourseTerm);

        Optional<CourseTerm> deletedCourseTerm = courseTermRepository.findById(savedCourseTerm.getId());
        assertThat(deletedCourseTerm).isEmpty();
    }

    @Test
    public void shouldFindAllCourseTerms() {
        Course course1 = new Course();
        entityManager.persist(course1);
        Term term1 = new Term();
        entityManager.persist(term1);

        Course course2 = new Course();
        entityManager.persist(course2);
        Term term2 = new Term();
        entityManager.persist(term2);

        CourseTerm courseTerm1 = new CourseTerm(course1, term1);
        entityManager.persist(courseTerm1);
        CourseTerm courseTerm2 = new CourseTerm(course2, term2);
        entityManager.persist(courseTerm2);

        List<CourseTerm> courseTerms = courseTermRepository.findAll();
        assertThat(courseTerms).hasSize(2).contains(courseTerm1, courseTerm2);
    }

    @Test
    public void shouldThrowExceptionWhenCourseOrTermIsNull() {
        Course course = new Course();
        entityManager.persist(course);
        Term term = null;

        CourseTerm courseTerm = new CourseTerm(course, term);
        org.junit.jupiter.api.Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            courseTermRepository.save(courseTerm);
        });
    }
}

