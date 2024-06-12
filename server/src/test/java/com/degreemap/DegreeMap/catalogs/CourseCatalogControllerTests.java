package com.degreemap.DegreeMap.catalogs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseCatalogController.class)
public class CourseCatalogControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseCatalogRepository ccRepository;

    @Test
    void deleteMe() { }  
    // This file tests CONTROLLERS.
    // Follow UserControllerTests.java for reference when making tests for this class.
    // Do NOT use UserRepositoryTests.java as reference, that file is used for testing repositories.\
    
    /* 
    You don't have to think of much edge cases. Just make sure you cover every outcome for a Controller method.
    Just look at the return statements, if a Controller method has multiple return statements then make a test method for each.
    */
}
