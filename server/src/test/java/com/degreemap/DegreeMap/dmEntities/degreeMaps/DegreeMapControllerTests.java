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

import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapController;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapRepository;

import java.util.Optional;

@WebMvcTest(DegreeMapController.class)
public class DegreeMapControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DegreeMapRepository degreeMapRepository;

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

        mockMvc.perform(get("/api/degreeMaps/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mechanical Engineering"));
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
}
