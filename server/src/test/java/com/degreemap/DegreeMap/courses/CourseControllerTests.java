package com.degreemap.DegreeMap.courses;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseController;
import java.util.Optional;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogRepository;

@WebMvcTest(CourseController.class)
public class CourseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CourseCatalogRepository courseCatalogRepository;

    @Test
    public void testCreateCourse() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        catalog.setId(1L);
        Course course = new Course(catalog, "Programming Concepts", "CS104", 3, "MIT", "Engineering", "Computer Science");
        given(courseCatalogRepository.findById(1L)).willReturn(Optional.of(catalog));
        given(courseRepository.save(any(Course.class))).willReturn(course);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Programming Concepts\", \"courseCatalogID\":1, \"courseCode\":\"CS104\", \"credits\":3, \"institution\":\"MIT\", \"college\":\"Engineering\", \"department\":\"Computer Science\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Programming Concepts"));
    }

    @Test
    public void testCreateCourseNullName() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        catalog.setId(1L);
        Course course = new Course(catalog, "Programming Concepts", "CS104", 3, "MIT", "Engineering", "Computer Science");
        given(courseCatalogRepository.findById(1L)).willReturn(Optional.of(catalog));
        given(courseRepository.save(any(Course.class))).willReturn(course);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\", \"courseCatalogID\":1, \"courseCode\":\"CS104\", \"credits\":3, \"institution\":\"MIT\", \"college\":\"Engineering\", \"department\":\"Computer Science\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreateCourseNullCatalog() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        catalog.setId(1L);
        Course course = new Course(catalog, "Programming Concepts", "CS104", 3, "MIT", "Engineering", "Computer Science");
        given(courseCatalogRepository.findById(1L)).willReturn(Optional.of(catalog));
        given(courseRepository.save(any(Course.class))).willReturn(course);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\", \"courseCatalogID\":\"\", \"courseCode\":\"CS104\", \"credits\":3, \"institution\":\"MIT\", \"college\":\"Engineering\", \"department\":\"Computer Science\"}"))
                .andExpect(status().isNotFound()); // Catalog id not found.
    }

    @Test
    public void testGetCourseByIdNotFound() throws Exception {
        given(courseRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/courses/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateCourse() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        catalog.setId(1L);
        Course course = new Course(catalog, "Data Science", "CS105", 4, "MIT", "Engineering", "Computer Science");
        course.setId(1L);

        given(courseRepository.findById(1L)).willReturn(Optional.of(course));
        given(courseRepository.save(any(Course.class))).willReturn(course);

        mockMvc.perform(put("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Advanced Data Science\", \"courseCode\":\"CS106\", \"credits\":4, \"institution\":\"MIT\", \"college\":\"Engineering\", \"department\":\"Computer Science\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Advanced Data Science"));
    }

    @Test
    public void testUpdateCourseNullName() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        catalog.setId(1L);
        Course course = new Course(catalog, "Data Science", "CS105", 4, "MIT", "Engineering", "Computer Science");
        course.setId(1L);

        given(courseRepository.findById(1L)).willReturn(Optional.of(course));
        given(courseRepository.save(any(Course.class))).willReturn(course);

        mockMvc.perform(put("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\", \"courseCode\":\"CS106\", \"credits\":4, \"institution\":\"MIT\", \"college\":\"Engineering\", \"department\":\"Computer Science\"}"))
            .andExpect(status().isConflict());
    }


    @Test
    public void testUpdateCourseNotFound() throws Exception {
        given(courseRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Advanced Data Science\", \"courseCode\":\"CS106\", \"credits\":4, \"institution\":\"MIT\", \"college\":\"Engineering\", \"department\":\"Computer Science\"}"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCourse() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        catalog.setId(1L);
        Course course = new Course(catalog, "Intro to AI", "CS107", 3, "MIT", "Engineering", "Computer Science");
        course.setId(1L);

        given(courseRepository.findById(1L)).willReturn(Optional.of(course));

        mockMvc.perform(delete("/api/courses/1"))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCourseNotFound() throws Exception {
        given(courseRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/courses/1"))
            .andExpect(status().isNotFound());
    }

}
