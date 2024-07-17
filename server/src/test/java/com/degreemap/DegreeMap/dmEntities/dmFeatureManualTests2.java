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

import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapController;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapRepository;
import com.degreemap.DegreeMap.dmEntities.terms.Term;
import com.degreemap.DegreeMap.dmEntities.terms.TermController;
import com.degreemap.DegreeMap.dmEntities.terms.TermRepository;
import com.degreemap.DegreeMap.dmEntities.years.Year;
import com.degreemap.DegreeMap.dmEntities.years.YearController;
import com.degreemap.DegreeMap.dmEntities.years.YearRepository;

@WebMvcTest(controllers = {DegreeMapController.class, YearController.class, TermController.class})
public class dmFeatureManualTests2 {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DegreeMapRepository degreeMapRepository;
    @MockBean
    private YearRepository yearRepository;
    @MockBean
    private TermRepository termRepository;
    
    // Test for manually printing out DegreeMaps
    // In VSCode, only run this test and go to Debug Console.

    /*
     * For this test, data output should look like this:
     
    {
        "id":1,
        "name":"SE Associates Degree",
        "years":[
                    {
                        "id":3,
                        "name":"2022-2023",
                        "terms":[
                            {"id":8,"name":"Spring"},
                            {"id":9,"name":"Summer"},
                            {"id":7,"name":"Fall"}
                        ]
                    },
                    {
                        "id":2,
                        "name":"2021-2022",
                        "terms":[
                            {"id":6,"name":"Summer"},
                            {"id":4,"name":"Fall"},
                            {"id":5,"name":"Spring"}
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
    }
}
