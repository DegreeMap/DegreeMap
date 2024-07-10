package com.degreemap.DegreeMap.courseEntities.tags;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(TagController.class)
public class TagControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagRepository tagRepository;

    @Test
    public void createTag_ReturnsTag() throws Exception {
        Tag tag = new Tag("Web Development");
        given(tagRepository.save(any(Tag.class))).willReturn(tag);

        mockMvc.perform(post("/api/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Web Development\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Web Development"));
    }

    @Test
    public void getTagById_Found_ReturnsTag() throws Exception {
        Tag tag = new Tag("Machine Learning");
        tag.setId(1L);
        given(tagRepository.findById(1L)).willReturn(Optional.of(tag));

        mockMvc.perform(get("/api/tags/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Machine Learning"));
    }

    @Test
    public void getAllTags_ReturnsListOfTags() throws Exception {
        Tag tag1 = new Tag("AI");
        Tag tag2 = new Tag("Big Data");
        given(tagRepository.findAll()).willReturn(Arrays.asList(tag1, tag2));

        mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void updateTag_Found_UpdatesAndReturnsTag() throws Exception {
        Tag existingTag = new Tag("AI");
        existingTag.setId(1L);
        given(tagRepository.findById(1L)).willReturn(Optional.of(existingTag));
        given(tagRepository.save(any(Tag.class))).willReturn(existingTag);

        mockMvc.perform(put("/api/tags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Advanced AI\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Advanced AI"));
    }

    @Test
    public void deleteTag_Found_DeletesAndReturnsOk() throws Exception {
        Tag tag = new Tag("Cloud Computing");
        tag.setId(1L);
        given(tagRepository.findById(1L)).willReturn(Optional.of(tag));

        mockMvc.perform(delete("/api/tags/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTagById_NotFound_ReturnsNotFound() throws Exception {
        given(tagRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/tags/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTag_NotFound_ReturnsNotFound() throws Exception {
        given(tagRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/tags/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Advanced AI\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTag_NotFound_ReturnsNotFound() throws Exception {
        given(tagRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(delete("/api/tags/1"))
                .andExpect(status().isNotFound());
    }
}
