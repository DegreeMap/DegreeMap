package com.degreemap.DegreeMap.dmEntities.courseTerms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.degreemap.DegreeMap.config.JpaTestConfig;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogRepository;
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.terms.Term;
import com.degreemap.DegreeMap.dmEntities.terms.TermRepository;
import com.degreemap.DegreeMap.dmEntities.years.Year;

import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

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
        Term found = termRepository.findById(term.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getName(), term.getName());
        assertEquals(found.getYear().getName(), year.getName());

        CourseTerm courseTerm = new CourseTerm(course, term);
        CourseTerm savedCourseTerm = courseTermRepository.save(courseTerm);
        assertThat(savedCourseTerm).isNotNull();
        assertThat(savedCourseTerm.getId()).isNotNull();
        assertThat(savedCourseTerm.getCourse()).isEqualTo(course);
        assertThat(savedCourseTerm.getTerm()).isEqualTo(term);
    }

    @Test
    public void shouldFindCourseTermById() {
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
        Term found = termRepository.findById(term.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getName(), term.getName());
        assertEquals(found.getYear().getName(), year.getName());

        CourseTerm courseTerm = new CourseTerm(course, term);
        CourseTerm savedCourseTerm = entityManager.persistFlushFind(courseTerm);

        Optional<CourseTerm> foundCourseTerm = courseTermRepository.findById(savedCourseTerm.getId());
        assertThat(foundCourseTerm).isPresent();
        assertThat(foundCourseTerm.get().getCourse()).isEqualTo(course);
        assertThat(foundCourseTerm.get().getTerm()).isEqualTo(term);
    }

    @Test
    public void shouldDeleteCourseTerm() {
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
        Term found = termRepository.findById(term.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getName(), term.getName());
        assertEquals(found.getYear().getName(), year.getName());

        CourseTerm courseTerm = new CourseTerm(course, term);
        CourseTerm savedCourseTerm = entityManager.persistFlushFind(courseTerm);
        assertThat(savedCourseTerm).isNotNull();
        assertThat(savedCourseTerm.getId()).isNotNull();
        assertThat(savedCourseTerm.getCourse()).isEqualTo(course);
        assertThat(savedCourseTerm.getTerm()).isEqualTo(term);

        courseTermRepository.delete(savedCourseTerm);

        Optional<CourseTerm> deletedCourseTerm = courseTermRepository.findById(savedCourseTerm.getId());
        assertThat(deletedCourseTerm).isEmpty();
        assertEquals(entityManager.find(Course.class, savedCourse.getId()), course);
        Term found2 = termRepository.findById(term.getId()).orElse(null);
        assertNotNull(found2);
        assertEquals(found2.getName(), term.getName());
        assertEquals(found2.getYear().getName(), year.getName());
    }

    @Test
    public void shouldFindAllCourseTerms() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);
        Course course1 = new Course(catalog, "Data Structures", "CS101", 4, "MIT", "Engineering", "Computer Science");
        Course savedCourse = courseRepository.save(course1);
        CourseCatalog savedCatalog = courseCatalogRepository.save(course1.getCourseCatalog());
        assertEquals(entityManager.find(Course.class, savedCourse.getId()), course1);
        assertEquals(savedCatalog.getCourses().size(), 1);
        assertEquals(savedCatalog.getCourses().contains(savedCourse), true);
        entityManager.persist(course1);

        DegreeMap dm = new DegreeMap("College");
        entityManager.persist(dm);
        Year year = new Year("2021-2022", dm); 
        entityManager.persist(year);
        Term term1 = new Term("Fall 2021", year);
        entityManager.persist(term1);
        entityManager.flush();
        Term found = termRepository.findById(term1.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getName(), term1.getName());
        assertEquals(found.getYear().getName(), year.getName());

        CourseCatalog catalog2 = new CourseCatalog("Engineering");
        entityManager.persist(catalog2);
        Course course2 = new Course(catalog2, "Data Structures", "CS101", 4, "MIT", "Engineering", "Computer Science");
        Course savedCourse2 = courseRepository.save(course2);
        CourseCatalog savedCatalog2 = courseCatalogRepository.save(course2.getCourseCatalog());
        assertEquals(entityManager.find(Course.class, savedCourse2.getId()), course2);
        assertEquals(savedCatalog2.getCourses().size(), 1);
        assertEquals(savedCatalog2.getCourses().contains(savedCourse2), true);
        entityManager.persist(course2);

        DegreeMap dm2 = new DegreeMap("College");
        entityManager.persist(dm2);
        Year year2 = new Year("2021-2022", dm2); 
        entityManager.persist(year2);
        Term term2 = new Term("Fall 2021", year2);
        entityManager.persist(term2);
        entityManager.flush();
        Term found2 = termRepository.findById(term2.getId()).orElse(null);
        assertNotNull(found2);
        assertEquals(found2.getName(), term2.getName());
        assertEquals(found2.getYear().getName(), year2.getName());

        CourseTerm courseTerm1 = new CourseTerm(course1, term1);
        entityManager.persist(courseTerm1);
        CourseTerm courseTerm2 = new CourseTerm(course2, term2);
        entityManager.persist(courseTerm2);

        List<CourseTerm> courseTerms = courseTermRepository.findAll();
        assertThat(courseTerms).hasSize(2).contains(courseTerm1, courseTerm2);
    }

    @Test
    public void shouldThrowExceptionWhenCourseOrTermIsNull() {
        org.junit.jupiter.api.Assertions.assertThrows(PropertyValueException.class, () -> {
            // Excuse the horrible method declaration
            Course course = new Course();
            entityManager.persist(course);
            Term term = null;
    
            CourseTerm courseTerm = new CourseTerm(course, term);
            courseTermRepository.save(courseTerm);
        });
    }
}

