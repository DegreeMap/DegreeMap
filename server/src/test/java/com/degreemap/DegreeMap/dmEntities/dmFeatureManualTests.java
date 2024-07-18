package com.degreemap.DegreeMap.dmEntities;

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
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseController;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.dmEntities.blocks.Block;
import com.degreemap.DegreeMap.dmEntities.blocks.BlockController;
import com.degreemap.DegreeMap.dmEntities.blocks.BlockRepository;
import com.degreemap.DegreeMap.dmEntities.courseTerms.CourseTerm;
import com.degreemap.DegreeMap.dmEntities.courseTerms.CourseTermController;
import com.degreemap.DegreeMap.dmEntities.courseTerms.CourseTermRepository;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapController;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapRepository;
import com.degreemap.DegreeMap.dmEntities.terms.Term;
import com.degreemap.DegreeMap.dmEntities.terms.TermController;
import com.degreemap.DegreeMap.dmEntities.terms.TermRepository;
import com.degreemap.DegreeMap.dmEntities.years.Year;
import com.degreemap.DegreeMap.dmEntities.years.YearController;
import com.degreemap.DegreeMap.dmEntities.years.YearRepository;

@WebMvcTest(controllers = {DegreeMapController.class, YearController.class, TermController.class, BlockController.class, CourseTermController.class, CourseController.class, CourseCatalogController.class})
public class dmFeatureManualTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DegreeMapRepository degreeMapRepository;
    @MockBean
    private YearRepository yearRepository;
    @MockBean
    private TermRepository termRepository;
    @MockBean
    private BlockRepository blockRepository;
    @MockBean
    private CourseTermRepository courseTermRepository;

    @MockBean
    private CourseCatalogRepository courseCatalogRepository;
    @MockBean
    private CourseRepository courseRepository;
    
    // Test for manually printing out DegreeMaps
    // In VSCode, only run this test and go to Debug Console.

    /*
     * For this test, data output should look like this:
        !!!! DegreeMap Data !!!! 
        {
          "id": 1,
          "name": "SE Associates Degree",
          "years": [
            {
              "id": 2,
              "name": "2021-2022",
              "terms": [
                {
                  "id": 4,
                  "name": "Fall",
                  "block": null,
                  "courseTerms": [
                    {
                      "id": 1,
                      "course": {
                        "id": 2,
                        "name": "SoftDev I",
                        "courseCode": "GCIS-123",
                        "credits": 4,
                        "institution": "RIT",
                        "college": "Golisano",
                        "department": "Software Engineering"
                      }
                    }
                  ]
                },
                {
                  "id": 5,
                  "name": "Spring",
                  "block": null,
                  "courseTerms": [
                    {
                      "id": 2,
                      "course": {
                        "id": 4,
                        "name": "Personal SoftDev",
                        "courseCode": "SWEN-250",
                        "credits": 4,
                        "institution": "RIT",
                        "college": "Golisano",
                        "department": "Software Engineering"
                      }
                    }
                  ]
                },
                {
                  "id": 6,
                  "name": "Summer",
                  "block": {
                    "id": 1,
                    "name": "Summer Break"
                  },
                  "courseTerms": []
                }
              ]
            },
            {
              "id": 3,
              "name": "2022-2023",
              "terms": [
                {
                  "id": 7,
                  "name": "Fall",
                  "block": null,
                  "courseTerms": []
                },
                {
                  "id": 8,
                  "name": "Spring",
                  "block": null,
                  "courseTerms": []
                },
                {
                  "id": 9,
                  "name": "Summer",
                  "block": {
                    "id": 2,
                    "name": "Summer Break"
                  },
                  "courseTerms": []
                }
              ]
            }
          ]
        }
    */
    @Test
    public void manuallyTestDegreeMaps() throws Exception {
        DegreeMap degreeMap = new DegreeMap("SE Associates Degree");
        degreeMap.setId(1L);
        given(degreeMapRepository.save(any(DegreeMap.class))).willReturn(degreeMap);
        given(degreeMapRepository.findById(1L)).willReturn(Optional.of(degreeMap));

        mockMvc.perform(post("/api/degreeMaps")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"SE Associates Degree\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SE Associates Degree"));

        Year year = new Year("2021-2022", degreeMap);
        year.setId(2L);
        degreeMap.getYears().add(year);
        Year year2 = new Year("2022-2023", degreeMap);
        year2.setId(3L);
        degreeMap.getYears().add(year2);
        
        Term fall = new Term("Fall", year);
        fall.setId(4L);
        year.addTerm(fall);
        Term spring = new Term("Spring", year);
        spring.setId(5L);
        year.addTerm(spring);
        Term summer = new Term("Summer", year);
        summer.setId(6L);
        year.addTerm(summer);

        Term fall2 = new Term("Fall", year2);
        fall2.setId(7L);
        year2.addTerm(fall2);
        Term spring2 = new Term("Spring", year2);
        spring2.setId(8L);
        year2.addTerm(spring2);
        Term summer2 = new Term("Summer", year2);
        summer2.setId(9L);
        year2.addTerm(summer2);

        MvcResult result = mockMvc.perform(get("/api/degreeMaps/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SE Associates Degree"))
                .andReturn();

        System.out.println("\n!!!! DegreeMap Data !!!! \n" + result.getResponse().getContentAsString());

        // Adding 2 courses and 1 block

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

        Course swen250 = new Course(catalog, "Personal SoftDev", "SWEN-250", 4, "RIT", "Golisano", "Software Engineering");
        swen250.setId(4L);
        given(courseRepository.findById(4L)).willReturn(Optional.of(swen250));
        MvcResult swen250Result = mockMvc.perform(get("/api/courses/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Personal SoftDev"))
                .andReturn();
        System.out.println("\n!!!! SWEN-250 Data !!!! \n" + swen250Result.getResponse().getContentAsString());

        Block summerBlock = new Block("Summer Break", summer);
        summerBlock.setId(1L);
        given(blockRepository.findById(1L)).willReturn(Optional.of(summerBlock));
        MvcResult blockResult = mockMvc.perform(get("/api/blocks/1"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("\n!!!! Summer Block 1 Data !!!! \n" + blockResult.getResponse().getContentAsString());

        Block summerBlock2 = new Block("Summer Break", summer2);
        summerBlock2.setId(2L);
        given(blockRepository.findById(2L)).willReturn(Optional.of(summerBlock2));
        MvcResult blockResult2 = mockMvc.perform(get("/api/blocks/2"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("\n!!!! Summer Block 2 Data !!!! \n" + blockResult2.getResponse().getContentAsString());

        CourseTerm term123 = new CourseTerm(gcis123, fall);
        term123.setId(1L);
        given(courseTermRepository.findById(1L)).willReturn(Optional.of(term123));
        MvcResult ct123 = mockMvc.perform(get("/api/courseTerms/1"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("\n!!!! CourseTerm 123 Data !!!! \n" + ct123.getResponse().getContentAsString());
        
        CourseTerm term250 = new CourseTerm(swen250, spring);
        term250.setId(2L);
        given(courseTermRepository.findById(2L)).willReturn(Optional.of(term250));
        MvcResult ct250 = mockMvc.perform(get("/api/courseTerms/2"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("\n!!!! CourseTerm 250 Data !!!! \n" + ct250.getResponse().getContentAsString());

        MvcResult updatedResult = mockMvc.perform(get("/api/degreeMaps/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SE Associates Degree"))
                .andReturn();

        System.out.println("\n!!!! DegreeMap Data !!!! \n" + updatedResult.getResponse().getContentAsString());
    }
}
