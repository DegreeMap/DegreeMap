package com.degreemap.DegreeMap.dmEntities.degreeMaps;

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
public class DegreeMapRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DegreeMapRepository degreeMapRepository;

    @Test
    public void whenSaveDegreeMap_thenFindById() {
        DegreeMap degreeMap = new DegreeMap("Computer Science");
        entityManager.persist(degreeMap);
        entityManager.flush();

        DegreeMap found = degreeMapRepository.findById(degreeMap.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getName(), degreeMap.getName());
    }

    @Test
    public void whenSaveDegreeMap_NullFields_thenFailure() {
        assertThrows(IllegalArgumentException.class, () -> {
            DegreeMap dm = new DegreeMap(null);
            degreeMapRepository.save(dm);
        });
    }

    @Test
    public void whenDeleteDegreeMap_thenNotFound() {
        DegreeMap degreeMap = new DegreeMap("Electrical Engineering");
        entityManager.persist(degreeMap);
        entityManager.flush();

        degreeMapRepository.delete(degreeMap);
        DegreeMap found = degreeMapRepository.findById(degreeMap.getId()).orElse(null);
        assertNull(found);
    }
}
