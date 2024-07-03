package com.degreemap.DegreeMap.dmEntities.degreeMaps;

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
import org.springframework.test.web.servlet.MvcResult;

import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapController;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapRepository;
import com.degreemap.DegreeMap.dmEntities.years.Year;
import com.degreemap.DegreeMap.dmEntities.years.YearController;
import com.degreemap.DegreeMap.dmEntities.years.YearRepository;

import java.util.Optional;

@WebMvcTest(controllers = {DegreeMapController.class, YearController.class})
public class DegreeMapControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DegreeMapRepository degreeMapRepository;
    @MockBean
    private YearRepository yearRepository;

    @Test
    public void createDegreeMap_ReturnsDegreeMap() throws Exception {
        DegreeMap degreeMap = new DegreeMap("Business Administration");
        given(degreeMapRepository.save(any(DegreeMap.class))).willReturn(degreeMap);

        mockMvc.perform(post("/api/degreeMaps")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Business Administration\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Business Administration"));
    }

    @Test
    public void getDegreeMapById_Found_ReturnsDegreeMap() throws Exception {
        DegreeMap degreeMap = new DegreeMap("Mechanical Engineering");
        degreeMap.setId(1L);
        given(degreeMapRepository.findById(1L)).willReturn(Optional.of(degreeMap));

        MvcResult result = mockMvc.perform(get("/api/degreeMaps/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mechanical Engineering"))
                .andReturn();

        System.out.println("wocwoucowucowucowvo  " + result.getResponse().getContentAsString());
    }

    @Test
    public void getDegreeMapById_NotFound_ReturnsError() throws Exception {
        DegreeMap degreeMap = new DegreeMap("Mechanical Engineering");
        degreeMap.setId(1L);
        given(degreeMapRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/degreeMaps/1"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void updateDegreeMap_Found_UpdatesAndReturnsDegreeMap() throws Exception {
        DegreeMap existingDegreeMap = new DegreeMap("Information Technology");
        existingDegreeMap.setId(1L);
        given(degreeMapRepository.findById(1L)).willReturn(Optional.of(existingDegreeMap));
        given(degreeMapRepository.save(any(DegreeMap.class))).willReturn(existingDegreeMap);

        mockMvc.perform(put("/api/degreeMaps/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated IT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated IT"));
    }

    @Test
    public void updateDegreeMap_NotFound_ReturnsError() throws Exception {
        given(degreeMapRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/degreeMaps/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated IT\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteDegreeMap_Found_DeletesAndReturnsOk() throws Exception {
        DegreeMap degreeMap = new DegreeMap("Software Engineering");
        degreeMap.setId(1L);
        given(degreeMapRepository.findById(1L)).willReturn(Optional.of(degreeMap));

        mockMvc.perform(delete("/api/degreeMaps/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteDegreeMap_NotFound_ReturnsNotFound() throws Exception {
        given(degreeMapRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/degreeMaps/1"))
                .andExpect(status().isNotFound());
    }

    // Test for manually printing out DegreeMaps
    // In VSCode, only run this test and go to Debug Console.

    /*
     * For this test, data output should look like this:
     
    {
        "id":1,
        "name":"Business Administration",
        "years":[
            {
                "id":null,
                "name":"2021-2022"
            }
        ]
    }
     
     */
    @Test
    public void manuallyTestDegreeMaps() throws Exception {
        DegreeMap degreeMap = new DegreeMap("Business Administration");
        degreeMap.setId(1L);
        given(degreeMapRepository.save(any(DegreeMap.class))).willReturn(degreeMap);
        given(degreeMapRepository.findById(1L)).willReturn(Optional.of(degreeMap));

        mockMvc.perform(post("/api/degreeMaps")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Business Administration\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Business Administration"));

        Year year = new Year("2021-2022", degreeMap);
        year.setId(2L);
        given(yearRepository.save(any(Year.class))).willReturn(year);
        given(yearRepository.findById(2L)).willReturn(Optional.of(year));

        mockMvc.perform(post("/api/years")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"2021-2022\",\"degreeMapId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("2021-2022"));
                
        MvcResult result = mockMvc.perform(get("/api/degreeMaps/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Business Administration"))
                .andReturn();

        System.out.println("!!!! DegreeMap Data !!!! ---> " + result.getResponse().getContentAsString());
    }
}
