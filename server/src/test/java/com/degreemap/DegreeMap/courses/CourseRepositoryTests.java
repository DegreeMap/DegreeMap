package com.degreemap.DegreeMap.courses;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.degreemap.DegreeMap.catalogs.CourseCatalogRepository;

@DataJpaTest
public class CourseRepositoryTests {
    @Autowired
    CourseCatalogRepository ccRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void deleteMe() { }  
}
