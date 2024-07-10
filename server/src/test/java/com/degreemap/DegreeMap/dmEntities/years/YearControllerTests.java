package com.degreemap.DegreeMap.dmEntities.years;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapRepository;

import java.util.Optional;

@WebMvcTest(YearController.class)
public class YearControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private YearRepository yearRepository;

    @MockBean
    private DegreeMapRepository degreeMapRepository;

    @Test
    public void createYear_ReturnsYear() throws Exception {
        DegreeMap degreeMap = new DegreeMap("Business Administration");
        degreeMap.setId(1L);
        Year year = new Year("2021-2022", degreeMap);
        given(degreeMapRepository.findById(1L)).willReturn(Optional.of(degreeMap));
        given(yearRepository.save(any(Year.class))).willReturn(year);

        mockMvc.perform(post("/api/years")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"2021-2022\",\"degreeMapId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("2021-2022"));
    }

    @Test
    public void getYearById_Found_ReturnsYear() throws Exception {
        DegreeMap degreeMap = new DegreeMap("Mechanical Engineering");
        degreeMap.setId(1L);
        Year year = new Year("2022-2023", degreeMap);
        year.setId(1L);
        given(yearRepository.findById(1L)).willReturn(Optional.of(year));

        mockMvc.perform(get("/api/years/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("2022-2023"));
    }

    @Test
    public void updateYear_Found_UpdatesAndReturnsYear() throws Exception {
        DegreeMap degreeMap = new DegreeMap("Information Technology");
        degreeMap.setId(1L);
        Year existingYear = new Year("2021-2022", degreeMap);
        existingYear.setId(1L);
        given(yearRepository.findById(1L)).willReturn(Optional.of(existingYear));
        given(yearRepository.save(any(Year.class))).willReturn(existingYear);

        mockMvc.perform(put("/api/years/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Year\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Year"));
    }

    @Test
    public void deleteYear_Found_DeletesAndReturnsOk() throws Exception {
        DegreeMap degreeMap = new DegreeMap("Software Engineering");
        degreeMap.setId(1L);
        Year year = new Year("2023-2024", degreeMap);
        year.setId(1L);
        given(yearRepository.findById(1L)).willReturn(Optional.of(year));

        mockMvc.perform(delete("/api/years/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getYearById_NotFound_ReturnsNotFound() throws Exception {
        given(yearRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/years/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateYearById_NotFound_ReturnsNotFound() throws Exception {
        given(yearRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/years/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Year\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteYear_NotFound_ReturnsNotFound() throws Exception {
        given(yearRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/years/1"))
                .andExpect(status().isNotFound());
    }
}
