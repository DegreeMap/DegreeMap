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
import com.degreemap.DegreeMap.courseEntities.corequisites.Corequisite;
import com.degreemap.DegreeMap.courseEntities.corequisites.CorequisiteController;
import com.degreemap.DegreeMap.courseEntities.corequisites.CorequisiteRepository;
import com.degreemap.DegreeMap.courseEntities.courseTags.CourseTag;
import com.degreemap.DegreeMap.courseEntities.courseTags.CourseTagController;
import com.degreemap.DegreeMap.courseEntities.courseTags.CourseTagRepository;
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseController;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.courseEntities.prerequisites.GradeRequirement;
import com.degreemap.DegreeMap.courseEntities.prerequisites.Prerequisite;
import com.degreemap.DegreeMap.courseEntities.prerequisites.PrerequisiteController;
import com.degreemap.DegreeMap.courseEntities.prerequisites.PrerequisiteRepository;
import com.degreemap.DegreeMap.courseEntities.tags.Tag;
import com.degreemap.DegreeMap.courseEntities.tags.TagController;
import com.degreemap.DegreeMap.courseEntities.tags.TagRepository;

@WebMvcTest(controllers = {CourseCatalogController.class, CourseController.class, TagController.class, CourseTagController.class, PrerequisiteController.class, CorequisiteController.class})
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
    @MockBean
    private PrerequisiteRepository prereqRepository;
    @MockBean
    private CorequisiteRepository coreqRepository;


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
    public void manuallyTestCourses() throws Exception {
        CourseCatalog catalog = new CourseCatalog("RIT Course Catalog");
        catalog.setId(1L);
        given(courseCatalogRepository.findById(1L)).willReturn(Optional.of(catalog));
        given(courseCatalogRepository.save(any(CourseCatalog.class))).willReturn(catalog);

        MvcResult catalogResult = mockMvc.perform(post("/api/course-catalogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + catalog.getName() + "\"}"))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").value("RIT Course Catalog"))
                        .andReturn();

        System.out.println("\n!!!! Catalog Data !!!! \n" + catalogResult.getResponse().getContentAsString());



        Course gcis123 = new Course(catalog, "SoftDev I", "GCIS-123", 4, "RIT", "Golisano", "Software Engineering");
        gcis123.setId(2L);
        given(courseRepository.findById(2L)).willReturn(Optional.of(gcis123));

        MvcResult gcis123Result = mockMvc.perform(get("/api/courses/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SoftDev I"))
                .andReturn();

        System.out.println("\n!!!! GCIS-123 Data !!!! \n" + gcis123Result.getResponse().getContentAsString());

        Tag tag = new Tag("Python");
        tag.setId(3L);
        given(tagRepository.findById(3L)).willReturn(Optional.of(tag));

        MvcResult tag123Result = mockMvc.perform(get("/api/tags/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Python"))
                .andReturn();

        System.out.println("\n!!!! GCIS-123 Tag Data !!!! \n" + tag123Result.getResponse().getContentAsString());

        Course swen250 = new Course(catalog, "Personal SoftDev", "SWEN-250", 4, "RIT", "Golisano", "Software Engineering");
        swen250.setId(4L);
        given(courseRepository.findById(4L)).willReturn(Optional.of(swen250));

        MvcResult swen250Result = mockMvc.perform(get("/api/courses/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Personal SoftDev"))
                .andReturn();
        System.out.println("\n!!!! SWEN-250 Data !!!! \n" + swen250Result.getResponse().getContentAsString());
        
        Tag tag250 = new Tag("C");
        tag250.setId(5L);
        given(tagRepository.findById(5L)).willReturn(Optional.of(tag250));

        MvcResult tag250Result = mockMvc.perform(get("/api/tags/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("C"))
                .andReturn();

        System.out.println("\n!!!! SWEN-250 Tag Data !!!! \n" + tag250Result.getResponse().getContentAsString());

        Course gcis124 = new Course(catalog, "SoftDev II", "GCIS-124", 4, "RIT", "Golisano", "Software Engineering");
        gcis124.setId(6L);
        given(courseRepository.findById(6L)).willReturn(Optional.of(gcis124));

        MvcResult gcis124Result = mockMvc.perform(get("/api/courses/6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SoftDev II"))
                .andReturn();

        System.out.println("\n!!!! GCIS-124 Data !!!! \n" + gcis124Result.getResponse().getContentAsString());

        Tag tag124 = new Tag("Java");
        tag124.setId(7L);
        given(tagRepository.findById(7L)).willReturn(Optional.of(tag124));

        MvcResult tag124Result = mockMvc.perform(get("/api/tags/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"))
                .andReturn();

        System.out.println("\n!!!! GCIS-124 Tag Data !!!! \n" + tag124Result.getResponse().getContentAsString());

        MvcResult updatedCatalogResult = mockMvc.perform(get("/api/course-catalogs/" + catalog.getId()))
                                    .andExpect(status().isOk())
                                    .andExpect(jsonPath("$.name").value("RIT Course Catalog"))
                                    .andReturn();

        System.out.println("\n!!!! Updated Catalog Data !!!! \n" + updatedCatalogResult.getResponse().getContentAsString());

        CourseTag courseTag123 = new CourseTag(gcis123, tag);
        courseTag123.setId(8L);
        given(courseTagRepository.findById(8L)).willReturn(Optional.of(courseTag123));
        CourseTag courseTag124 = new CourseTag(gcis124, tag124);
        courseTag124.setId(9L);
        given(courseTagRepository.findById(9L)).willReturn(Optional.of(courseTag124));
        CourseTag courseTag250 = new CourseTag(swen250, tag250);
        courseTag250.setId(10L);
        given(courseTagRepository.findById(10L)).willReturn(Optional.of(courseTag250));

        MvcResult ct123 = mockMvc.perform(get("/api/courseTags/8"))
                    .andExpect(status().isOk())
                    .andReturn();
        System.out.println("\n!!!! CourseTag 123 Data !!!! \n" + ct123.getResponse().getContentAsString());
                
        MvcResult ct124 = mockMvc.perform(get("/api/courseTags/9"))
                    .andExpect(status().isOk())
                    .andReturn();
        System.out.println("\n!!!! CourseTag 124 Data !!!! \n" + ct124.getResponse().getContentAsString());

        MvcResult ct250 = mockMvc.perform(get("/api/courseTags/10"))
                    .andExpect(status().isOk())
                    .andReturn();
        System.out.println("\n!!!! CourseTag 250 Data !!!! \n" + ct250.getResponse().getContentAsString());

        Prerequisite prereq123 = new Prerequisite(GradeRequirement.C_PLUS, gcis123, gcis124);
        prereq123.setId(11L);
        given(prereqRepository.findById(11L)).willReturn(Optional.of(prereq123));
        
        MvcResult prereqResult = mockMvc.perform(get("/api/prerequisites/11"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("\n!!!! Prerequisite Data !!!! \n" + prereqResult.getResponse().getContentAsString());

        Corequisite coreq = new Corequisite(gcis124, swen250);
        coreq.setId(12L);
        given(coreqRepository.findById(12L)).willReturn(Optional.of(coreq));
        MvcResult coreqResult = mockMvc.perform(get("/api/corequisites/12"))
                .andExpect(status().isOk())        
                .andReturn();
        System.out.println("\n!!!! Corequisite Data !!!! \n" + coreqResult.getResponse().getContentAsString());
    }
}
