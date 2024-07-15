package com.degreemap.DegreeMap.courseEntities;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogController;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogRepository;
import com.degreemap.DegreeMap.courseEntities.courseTags.CourseTagController;
import com.degreemap.DegreeMap.courseEntities.courseTags.CourseTagRepository;
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseController;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.courseEntities.tags.Tag;
import com.degreemap.DegreeMap.courseEntities.tags.TagController;
import com.degreemap.DegreeMap.courseEntities.tags.TagRepository;

@WebMvcTest(controllers = {CourseCatalogController.class, CourseController.class, TagController.class, CourseTagController.class})
public class courseFeatureManualTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseCatalogRepository courseCatalogRepository;
    @MockBean
    private CourseRepository courseRepository;
    @MockBean
    private TagRepository tagRepository;
    @MockBean
    private CourseTagRepository courseTagRepository;


    // Test for manually printing out Courses
    // In VSCode, only run this test and go to Debug Console.

    /**
    Manual tests for ALL course features:

    -- COURSES --
    First print (testing baseline course feature):
    {
        "id":2,
        "name":"Programming Concepts",
        "courseCode":"CS104",
        "credits":3,
        "institution":"MIT",
        "college":"Engineering",
        "department":"Computer Science"
    }

    -- TAGS & COURSETAGS --
    Second print (testing baseline tag feature):
    {
        "id":3,
        "name":"Machine Learning"
    }

    **/

    @Test
    public void manuallyTestDegreeMaps() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Engineering");
        catalog.setId(1L);
        Course course = new Course(catalog, "Programming Concepts", "CS104", 3, "MIT", "Engineering", "Computer Science");
        course.setId(2L);
        given(courseCatalogRepository.findById(1L)).willReturn(Optional.of(catalog));
        given(courseRepository.findById(2L)).willReturn(Optional.of(course));

        MvcResult result = mockMvc.perform(get("/api/courses/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Programming Concepts"))
                .andReturn();

        System.out.println("\n!!!! Baseline Course Data !!!! \n" + result.getResponse().getContentAsString());

        Tag tag = new Tag("Machine Learning");
        tag.setId(3L);
        given(tagRepository.findById(3L)).willReturn(Optional.of(tag));

        MvcResult tagResult = mockMvc.perform(get("/api/tags/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Machine Learning"))
                .andReturn();

        System.out.println("\n!!!! Tag Data !!!! \n" + tagResult.getResponse().getContentAsString());
    }
}
