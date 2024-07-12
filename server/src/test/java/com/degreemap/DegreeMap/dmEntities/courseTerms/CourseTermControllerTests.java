package com.degreemap.DegreeMap.dmEntities.courseTerms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.dmEntities.terms.Term;
import com.degreemap.DegreeMap.dmEntities.terms.TermRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseTermController.class)
public class CourseTermControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseTermRepository ctRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private TermRepository termRepository;

    @Test
    public void createCourseTermTest() throws Exception {
        Course course = new Course(); // You might want to add specific fields
        course.setId(1L);
        Term term = new Term();
        term.setId(2L);

        CourseTerm courseTerm = new CourseTerm(course, term);
        courseTerm.setId(3L);

        given(courseRepository.findById(1L)).willReturn(Optional.of(course));
        given(termRepository.findById(2L)).willReturn(Optional.of(term));
        given(ctRepository.save(any(CourseTerm.class))).willReturn(courseTerm);

        mockMvc.perform(post("/api/courseTerms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"courseId\": 1, \"termId\": 2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L));
    }

    @Test
    public void getCourseTermByIdTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        Term term = new Term();
        term.setId(2L);

        CourseTerm courseTerm = new CourseTerm(course, term);
        courseTerm.setId(3L);

        given(ctRepository.findById(3L)).willReturn(Optional.of(courseTerm));

        mockMvc.perform(get("/api/courseTerms/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L));
    }

    @Test
    public void deleteCourseTermTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        Term term = new Term();
        term.setId(2L);

        CourseTerm courseTerm = new CourseTerm(course, term);
        courseTerm.setId(3L);

        given(ctRepository.findById(3L)).willReturn(Optional.of(courseTerm));

        mockMvc.perform(delete("/api/courseTerms/3"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllCourseTermsTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        Term term = new Term();
        term.setId(2L);

        CourseTerm courseTerm1 = new CourseTerm(course, term);
        courseTerm1.setId(3L);
        CourseTerm courseTerm2 = new CourseTerm(course, term);
        courseTerm2.setId(4L);

        List<CourseTerm> courseTerms = Arrays.asList(courseTerm1, courseTerm2);
        given(ctRepository.findAll()).willReturn(courseTerms);

        mockMvc.perform(get("/api/courseTerms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(3L))
                .andExpect(jsonPath("$[1].id").value(4L));
    }
}
