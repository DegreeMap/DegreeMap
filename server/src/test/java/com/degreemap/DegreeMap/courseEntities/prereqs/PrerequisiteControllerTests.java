package com.degreemap.DegreeMap.courseEntities.prereqs;

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

import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.courseEntities.prerequisites.GradeRequirement;
import com.degreemap.DegreeMap.courseEntities.prerequisites.Prerequisite;
import com.degreemap.DegreeMap.courseEntities.prerequisites.PrerequisiteController;
import com.degreemap.DegreeMap.courseEntities.prerequisites.PrerequisiteRepository;

import java.util.Optional;

@WebMvcTest(PrerequisiteController.class)
public class PrerequisiteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrerequisiteRepository prerequisiteRepository;

    @MockBean
    private CourseRepository courseRepository;

    @Test
    public void createPrerequisite_ValidDataAUpper_ReturnsCreated() throws Exception {
        Course prereqCourse = new Course();
        prereqCourse.setId(1L);
        Course connectedCourse = new Course();
        connectedCourse.setId(2L);
        Prerequisite prereq = new Prerequisite(GradeRequirement.A, prereqCourse, connectedCourse);

        given(courseRepository.findById(1L)).willReturn(Optional.of(prereqCourse));
        given(courseRepository.findById(2L)).willReturn(Optional.of(connectedCourse));
        given(prerequisiteRepository.save(any(Prerequisite.class))).willReturn(prereq);

        mockMvc.perform(post("/api/prerequisites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prereqCourseId\":1, \"connectedCourseId\":2, \"gradeRequirement\":\"A\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gradeRequirement").value("A"));
    }

    @Test
    public void createPrerequisite_ValidDataALower_ReturnsCreated() throws Exception {
        Course prereqCourse = new Course();
        prereqCourse.setId(1L);
        Course connectedCourse = new Course();
        connectedCourse.setId(2L);
        Prerequisite prereq = new Prerequisite(GradeRequirement.A, prereqCourse, connectedCourse);

        given(courseRepository.findById(1L)).willReturn(Optional.of(prereqCourse));
        given(courseRepository.findById(2L)).willReturn(Optional.of(connectedCourse));
        given(prerequisiteRepository.save(any(Prerequisite.class))).willReturn(prereq);

        mockMvc.perform(post("/api/prerequisites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prereqCourseId\":1, \"connectedCourseId\":2, \"gradeRequirement\":\"a\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void createPrerequisite_ValidDataNone_ReturnsCreated() throws Exception {
        Course prereqCourse = new Course();
        prereqCourse.setId(1L);
        Course connectedCourse = new Course();
        connectedCourse.setId(2L);
        Prerequisite prereq = new Prerequisite(GradeRequirement.NONE, prereqCourse, connectedCourse);

        given(courseRepository.findById(1L)).willReturn(Optional.of(prereqCourse));
        given(courseRepository.findById(2L)).willReturn(Optional.of(connectedCourse));
        given(prerequisiteRepository.save(any(Prerequisite.class))).willReturn(prereq);

        mockMvc.perform(post("/api/prerequisites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prereqCourseId\":1, \"connectedCourseId\":2, \"gradeRequirement\":\"NONE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gradeRequirement").value("NONE"));
    }

    @Test
    public void createPrerequisite_MissingFields_ReturnsConflict() throws Exception {
        mockMvc.perform(post("/api/prerequisites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prereqCourseId\":\"\", \"connectedCourseId\":2, \"gradeRequirement\":\"A\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void getAllPrerequisites_ReturnsList() throws Exception {
        mockMvc.perform(get("/api/prerequisites"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPrerequisiteById_Found_ReturnsPrerequisite() throws Exception {
        Prerequisite prereq = new Prerequisite();
        prereq.setId(1L);

        given(prerequisiteRepository.findById(1L)).willReturn(Optional.of(prereq));

        mockMvc.perform(get("/api/prerequisites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void getPrerequisiteById_NotFound_ReturnsNotFound() throws Exception {
        given(prerequisiteRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/prerequisites/1"))
                .andExpect(status().isConflict());
    }

    @Test
    public void deletePrerequisite_Found_DeletesPrerequisite() throws Exception {
        Prerequisite prereq = new Prerequisite();
        prereq.setId(1L);

        given(prerequisiteRepository.findById(1L)).willReturn(Optional.of(prereq));

        mockMvc.perform(delete("/api/prerequisites/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePrerequisite_NotFound_ReturnsNotFound() throws Exception {
        given(prerequisiteRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/prerequisites/1"))
                .andExpect(status().isConflict());
    }
}
