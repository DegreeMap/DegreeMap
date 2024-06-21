package com.degreemap.DegreeMap.courses;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogRepository;
import com.degreemap.DegreeMap.courseEntities.courses.CourseController;

@WebMvcTest(CourseController.class)
public class CourseControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseCatalogRepository ccRepository;

    @Test
    void deleteMe() { } 
}
