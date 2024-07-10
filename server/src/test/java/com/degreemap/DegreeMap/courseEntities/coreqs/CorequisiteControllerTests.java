package com.degreemap.DegreeMap.courseEntities.coreqs;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import com.degreemap.DegreeMap.courseEntities.corequisites.Corequisite;
import com.degreemap.DegreeMap.courseEntities.corequisites.CorequisiteController;
import com.degreemap.DegreeMap.courseEntities.corequisites.CorequisiteRepository;
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;

import java.util.Optional;

@WebMvcTest(CorequisiteController.class)
public class CorequisiteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CorequisiteRepository corequisiteRepository;

    @MockBean
    private CourseRepository courseRepository;

    @Test
    public void createCorequisite_ValidData_ReturnsCreated() throws Exception {
        Course coreqCourse = new Course();
        coreqCourse.setId(1L);
        Course connectedCourse = new Course();
        connectedCourse.setId(2L);
        Corequisite reciprocal = new Corequisite(connectedCourse, coreqCourse);
        Corequisite coreq = new Corequisite(coreqCourse, connectedCourse);

        given(courseRepository.findById(1L)).willReturn(Optional.of(coreqCourse));
        given(courseRepository.findById(2L)).willReturn(Optional.of(connectedCourse));
        given(corequisiteRepository.save(any(Corequisite.class)))
            .willReturn(reciprocal)
            .willReturn(coreq);

        mockMvc.perform(post("/api/corequisites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"coreqCourseId\":1, \"connectedCourseId\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coreqCourse.id").value(1L))
                .andExpect(jsonPath("$.connectedCourse.id").value(2L));
    }

    @Test
    public void createCorequisite_MissingFields_ReturnsConflict() throws Exception {
        mockMvc.perform(post("/api/corequisites")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"coreqCourseId\":1}"))
                .andExpect(status().isConflict());
    }

    @Test
    public void getAllCorequisites_ReturnsList() throws Exception {
        mockMvc.perform(get("/api/corequisites"))
                .andExpect(status().isOk());
    }

    @Test
    public void getCorequisiteById_Found_ReturnsCorequisite() throws Exception {
        Corequisite coreq = new Corequisite();
        coreq.setId(1L);

        given(corequisiteRepository.findById(1L)).willReturn(Optional.of(coreq));

        mockMvc.perform(get("/api/corequisites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void getCorequisiteById_NotFound_ReturnsNotFound() throws Exception {
        given(corequisiteRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/corequisites/1"))
                .andExpect(status().isConflict());
    }

    @Test
    public void deleteCorequisite_Found_DeletesCorequisiteAndReciprocal() throws Exception {
        Corequisite coreq = new Corequisite();
        coreq.setId(1L);
        Course coreqCourse = new Course();
        coreqCourse.setId(2L);
        Course connectedCourse = new Course();
        connectedCourse.setId(1L);

        coreq.setCoreqCourse(coreqCourse);
        coreq.setConnectedCoreqCourse(connectedCourse);

        Corequisite reciprocal = new Corequisite(connectedCourse, coreqCourse);

        given(corequisiteRepository.findById(1L)).willReturn(Optional.of(coreq));
        given(corequisiteRepository.findReciprocal(coreqCourse, connectedCourse)).willReturn(Optional.of(reciprocal));

        mockMvc.perform(delete("/api/corequisites/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCorequisite_NotFound_ReturnsNotFound() throws Exception {
        given(corequisiteRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/corequisites/1"))
                .andExpect(status().isConflict());
    }
}
