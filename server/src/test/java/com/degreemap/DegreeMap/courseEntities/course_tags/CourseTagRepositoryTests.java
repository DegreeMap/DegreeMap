package com.degreemap.DegreeMap.courseEntities.course_tags;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.degreemap.DegreeMap.config.JpaTestConfig;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.courseTags.CourseTag;
import com.degreemap.DegreeMap.courseEntities.courseTags.CourseTagRepository;
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.courseEntities.tags.Tag;
import com.degreemap.DegreeMap.courseEntities.tags.TagRepository;

@DataJpaTest
@Import(JpaTestConfig.class)
public class CourseTagRepositoryTests {

    // TODO update tests to test for the sets in the courses and tags.

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseTagRepository courseTagRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TagRepository tagRepository;

    @Test
    public void whenSaveCourseTag_thenFindById() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);
        Course course = new Course(catalog, "Object-Oriented Programming", "CS200", 3, "MIT", "Engineering", "Computer Science");
        entityManager.persist(course);

        Tag tag = new Tag("Analytics");
        entityManager.persist(tag);

        CourseTag courseTag = new CourseTag(course, tag);
        entityManager.persist(courseTag);
        entityManager.flush();

        CourseTag found = courseTagRepository.findById(courseTag.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getCourse().getName(), course.getName());
        assertEquals(found.getTag().getName(), tag.getName());
    }

    @Test
    public void whenSaveCourseTag_NullData_thenFailure() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            CourseTag courseTag = new CourseTag(null, null);
            courseTagRepository.save(courseTag);
        });

        assertTrue(exception.getMessage().contains("All fields must be filled for CourseTags"));
    }

    @Test
    public void whenDeleteCourseTag_thenNotFound() {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        entityManager.persist(catalog);
        Course course = new Course(catalog, "Object-Oriented Programming", "CS200", 3, "MIT", "Engineering", "Computer Science");
        entityManager.persist(course);

        Tag tag = new Tag("AI");
        entityManager.persist(tag);

        CourseTag courseTag = new CourseTag(course, tag);
        entityManager.persist(courseTag);
        entityManager.flush();

        courseTagRepository.delete(courseTag);

        CourseTag found = courseTagRepository.findById(courseTag.getId()).orElse(null);
        assertNull(found);
        Course foundCourse = courseRepository.findById(course.getId()).orElse(null);
        assertNotNull(foundCourse);
        assertEquals(foundCourse, course);
        Tag foundTag = tagRepository.findById(tag.getId()).orElse(null);
        assertNotNull(foundTag);
        assertEquals(foundTag, tag);
    }
}
