package com.degreemap.DegreeMap.courseEntities.catalogs;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseCatalogController.class)
public class CatalogControllerTests {
    
    // TODO test courses

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseCatalogRepository catalogRepository;

    @Test
    public void testCreateCatalog() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Computer Science");
        given(catalogRepository.save(any(CourseCatalog.class))).willReturn(catalog);

        mockMvc.perform(post("/api/course-catalogs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + catalog.getName() + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Computer Science"));
    }

    @Test
    public void testGetAllCatalogs() throws Exception {
        mockMvc.perform(get("/api/course-catalogs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetCatalogById() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Computer Science");
        catalog.setId(1L);
        given(catalogRepository.findById(1L)).willReturn(Optional.of(catalog));

        mockMvc.perform(get("/api/course-catalogs/" + catalog.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Computer Science"));
    }

    @Test
    public void testGetCatalogByIdNotFound() throws Exception {
        given(catalogRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/course-catalogs/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testEditingCatalog() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Computer Science");
        catalog.setId(1L);

        given(catalogRepository.findById(1L)).willReturn(Optional.of(catalog));
        given(catalogRepository.save(any(CourseCatalog.class))).willReturn(catalog); 

        mockMvc.perform(put("/api/course-catalogs/" + catalog.getId())
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"" + "Fart Science" + "\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Fart Science"));
    }

    @Test
    public void testEditingUserNotFound() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Computer Science");
        catalog.setId(1L);

        given(catalogRepository.findById(2L)).willReturn(Optional.of(catalog));
        given(catalogRepository.save(any(CourseCatalog.class))).willReturn(catalog); 

        mockMvc.perform(put("/api/users/" + catalog.getId())
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"" + "Fart Science" + "\"}"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCatalog() throws Exception {
        CourseCatalog catalog = new CourseCatalog("Computer Science");
        catalog.setId(1L);
        given(catalogRepository.findById(1L)).willReturn(Optional.of(catalog));

        mockMvc.perform(delete("/api/course-catalogs/" + catalog.getId()))
            .andExpect(status().isOk());

        verify(catalogRepository).delete(catalog);
    }

    @Test
    public void testDeleteCatalogNotFound() throws Exception {
        given(catalogRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/course-catalogs/1"))
            .andExpect(status().isNotFound());
    }
}
