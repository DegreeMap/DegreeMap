package com.degreemap.DegreeMap.dmEntities.blocks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.terms.Term;
import com.degreemap.DegreeMap.dmEntities.terms.TermRepository;
import com.degreemap.DegreeMap.dmEntities.years.Year;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BlockController.class)
public class BlockControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlockRepository blockRepository;

    @MockBean
    private TermRepository termRepository;

    @Test
    public void createBlockTest() throws Exception {
        DegreeMap dm = new DegreeMap("College");
        dm.setId(7L);
        Year year = new Year("2021-2022", dm);
        year.setId(1L);
        Term term = new Term("Fall 2021", year);
        term.setId(1L);
    
        Block block = new Block("Block 1", term);
        block.setId(1L);
    
        given(termRepository.findById(1L)).willReturn(Optional.of(term));
        given(blockRepository.save(any(Block.class))).willReturn(block);
    
        String jsonContent = "{\"name\":\"Block 1\", \"termId\":1}";
    
        mockMvc.perform(post("/api/blocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Block 1"));
    }
    

    @Test
    public void getBlockByIdTest() throws Exception {
        DegreeMap dm = new DegreeMap("College");
        dm.setId(7L);
        Year year = new Year("2021-2022", dm);
        year.setId(1L);
        Term term = new Term("Fall 2021", year);
        term.setId(1L);

        Block block = new Block("Block 1", term);
        block.setId(1L);

        given(blockRepository.findById(1L)).willReturn(Optional.of(block));

        mockMvc.perform(get("/api/blocks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Block 1"));
    }

    @Test
    public void updateBlockTest() throws Exception {
        DegreeMap dm = new DegreeMap("College");
        dm.setId(7L);
        Year year = new Year("2021-2022", dm);
        year.setId(1L);
        Term term = new Term("Fall 2021", year);
        term.setId(1L);
    
        Block block = new Block("Block 1", term);
        block.setId(1L);
    
        given(blockRepository.findById(1L)).willReturn(Optional.of(block));
        given(blockRepository.save(any(Block.class))).willReturn(block);
    
        String jsonContent = "{\"name\":\"Block 1 Updated\", \"termId\":1}";
    
        mockMvc.perform(put("/api/blocks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Block 1 Updated"));
    }
    

    @Test
    public void deleteBlockTest() throws Exception {
        DegreeMap dm = new DegreeMap("College");
        dm.setId(7L);
        Year year = new Year("2021-2022", dm);
        year.setId(1L);
        Term term = new Term("Fall 2021", year);
        term.setId(1L);

        Block block = new Block("Block 1", term);
        block.setId(1L);

        given(blockRepository.findById(1L)).willReturn(Optional.of(block));

        mockMvc.perform(delete("/api/blocks/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllBlocksTest() throws Exception {
        DegreeMap dm = new DegreeMap("College");
        dm.setId(7L);
        Year year = new Year("2021-2022", dm);
        year.setId(1L);
        Term term = new Term("Fall 2021", year);
        term.setId(1L);

        Block block1 = new Block("Block 1", term);
        block1.setId(1L);
        Block block2 = new Block("Block 2", term);
        block2.setId(2L);

        List<Block> blocks = Arrays.asList(block1, block2);

        given(blockRepository.findAll()).willReturn(blocks);

        mockMvc.perform(get("/api/blocks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Block 1"))
                .andExpect(jsonPath("$[1].name").value("Block 2"));
    }
}
