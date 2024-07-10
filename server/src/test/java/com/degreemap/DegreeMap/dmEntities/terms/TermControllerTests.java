package com.degreemap.DegreeMap.dmEntities.terms;

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
import com.degreemap.DegreeMap.dmEntities.years.Year;
import com.degreemap.DegreeMap.dmEntities.years.YearRepository;

import java.util.Optional;

@WebMvcTest(TermController.class)
public class TermControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TermRepository termRepository;

    @MockBean
    private YearRepository yearRepository;

    @Test
    public void createTerm_ReturnsTerm() throws Exception {
        DegreeMap dm = new DegreeMap("College");
        dm.setId(7L);
        Year year = new Year("2021-2022", dm);
        year.setId(1L);
        Term term = new Term("Fall 2021", year);
        given(yearRepository.findById(1L)).willReturn(Optional.of(year));
        given(termRepository.save(any(Term.class))).willReturn(term);

        mockMvc.perform(post("/api/terms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Fall 2021\",\"yearId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fall 2021"));
    }

    @Test
    public void getTermById_Found_ReturnsTerm() throws Exception {
        DegreeMap dm = new DegreeMap("College");
        dm.setId(7L);
        Year year = new Year("2022-2023", dm);
        year.setId(2L);
        Term term = new Term("Spring 2022", year);
        term.setId(1L);
        given(termRepository.findById(1L)).willReturn(Optional.of(term));

        mockMvc.perform(get("/api/terms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Spring 2022"));
    }

    @Test
    public void updateTerm_Found_UpdatesAndReturnsTerm() throws Exception {
        DegreeMap dm = new DegreeMap("College");
        dm.setId(7L);
        Year year = new Year("2023-2024", dm);
        year.setId(3L);
        Term existingTerm = new Term("Winter 2023", year);
        existingTerm.setId(1L);
        given(termRepository.findById(1L)).willReturn(Optional.of(existingTerm));
        given(termRepository.save(any(Term.class))).willReturn(existingTerm);

        mockMvc.perform(put("/api/terms/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Term\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Term"));
    }

    @Test
    public void deleteTerm_Found_DeletesAndReturnsOk() throws Exception {
        DegreeMap dm = new DegreeMap("College");
        dm.setId(7L);
        Year year = new Year("2024-2025", dm);
        year.setId(4L);
        Term term = new Term("Fall 2024", year);
        term.setId(1L);
        given(termRepository.findById(1L)).willReturn(Optional.of(term));

        mockMvc.perform(delete("/api/terms/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTermById_NotFound_ReturnsNotFound() throws Exception {
        given(termRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/terms/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTerm_NotFound_ReturnsNotFound() throws Exception {
        given(termRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/terms/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Updated Term\"}"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTerm_NotFound_ReturnsNotFound() throws Exception {
        given(termRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/terms/1"))
                .andExpect(status().isNotFound());
    }
}
