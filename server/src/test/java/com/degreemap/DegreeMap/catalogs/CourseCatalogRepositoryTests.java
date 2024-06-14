package com.degreemap.DegreeMap.catalogs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class CourseCatalogRepositoryTests {
    @Autowired
    CourseCatalogRepository ccRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void deleteMe() { }  
    // Follow UserRepositoryTests.java for reference when making tests for this class.
    // Do NOT use UserControllerTests.java as reference, that file is used for testing controllers.

    // For these you'll have to think of every test case regarding the actions you can do with repositories.
}
