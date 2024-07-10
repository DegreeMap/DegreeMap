package com.degreemap.DegreeMap.dmEntities.years;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.degreemap.DegreeMap.config.JpaTestConfig;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapRepository;

@DataJpaTest
@Import(JpaTestConfig.class)
public class YearRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private YearRepository yearRepository;
    @Autowired
    private DegreeMapRepository degreeMapRepository;

    @Test
    public void whenSaveYear_thenFindById() {
        DegreeMap degreeMap = new DegreeMap("Computer Science");
        entityManager.persist(degreeMap);

        Year year = new Year("2021-2022", degreeMap);
        entityManager.persist(year);
        entityManager.flush();

        Year found = yearRepository.findById(year.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getName(), year.getName());
        assertEquals(found.getDegreeMap().getId(), degreeMap.getId());
    }

    @Test
    public void whenDeleteYear_thenNotFound() {
        DegreeMap degreeMap = new DegreeMap("Electrical Engineering");
        entityManager.persist(degreeMap);
        Year year = new Year("2022-2023", degreeMap);
        entityManager.persist(year);
        entityManager.flush();

        yearRepository.delete(year);
        Year found = yearRepository.findById(year.getId()).orElse(null);
        assertNull(found);
        DegreeMap dm = degreeMapRepository.findById(degreeMap.getId()).get();
        assertNotNull(dm);
        assertEquals(dm.getName(), degreeMap.getName());
    }
}
