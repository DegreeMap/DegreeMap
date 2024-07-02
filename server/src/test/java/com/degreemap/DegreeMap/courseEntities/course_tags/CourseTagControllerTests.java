package com.degreemap.DegreeMap.courseEntities.course_tags;

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

import com.degreemap.DegreeMap.courseEntities.courseTags.CourseTag;
import com.degreemap.DegreeMap.courseEntities.courseTags.CourseTagController;
import com.degreemap.DegreeMap.courseEntities.courseTags.CourseTagRepository;
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.courseEntities.tags.Tag;
import com.degreemap.DegreeMap.courseEntities.tags.TagRepository;

import java.util.Optional;

@WebMvcTest(CourseTagController.class)
public class CourseTagControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseTagRepository courseTagRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private TagRepository tagRepository;

    @Test
    public void createCourseTag_ReturnsCourseTag() throws Exception {
        Course course = new Course();
        course.setId(1L);
        Tag tag = new Tag("Web Development");
        tag.setId(2L);
        CourseTag courseTag = new CourseTag(course, tag);

        given(courseRepository.findById(1L)).willReturn(Optional.of(course));
        given(tagRepository.findById(2L)).willReturn(Optional.of(tag));
        given(courseTagRepository.save(any(CourseTag.class))).willReturn(courseTag);

        mockMvc.perform(post("/api/courseTags")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"courseId\":1,\"tagId\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course.id").value(1L))
                .andExpect(jsonPath("$.tag.id").value(2L));
    }

    @Test
    public void createCourseTag_NotFoundFields_ReturnsNotFound() throws Exception {
        mockMvc.perform(post("/api/courseTags")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"courseId\":1,\"tagId\":2}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCourseTagById_Found_ReturnsCourseTag() throws Exception {
        Course course = new Course();
        course.setId(1L);
        Tag tag = new Tag("Machine Learning");
        tag.setId(2L);
        CourseTag courseTag = new CourseTag(course, tag);
        courseTag.setId(1L);

        given(courseTagRepository.findById(1L)).willReturn(Optional.of(courseTag));

        mockMvc.perform(get("/api/courseTags/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void getAllCourseTags_ReturnsList() throws Exception {
        mockMvc.perform(get("/api/courseTags"))
                .andExpect(status().isOk());
    }

    @Test
    public void getCourseTagById_NotFound_ReturnsNotFound() throws Exception {
        given(courseTagRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/courseTags/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCourseTag_Found_DeletesAndReturnsOk() throws Exception {
        CourseTag courseTag = new CourseTag();
        courseTag.setId(1L);

        given(courseTagRepository.findById(1L)).willReturn(Optional.of(courseTag));

        mockMvc.perform(delete("/api/courseTags/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCourseTag_NotFound_DeletesAndReturnsNotFound() throws Exception {
        given(courseTagRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/courseTags/1"))
                .andExpect(status().isNotFound());
    }
}
