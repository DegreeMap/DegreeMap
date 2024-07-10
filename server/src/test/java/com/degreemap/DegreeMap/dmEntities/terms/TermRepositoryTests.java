package com.degreemap.DegreeMap.dmEntities.terms;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.degreemap.DegreeMap.config.JpaTestConfig;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.years.Year;

@DataJpaTest
@Import(JpaTestConfig.class)
public class TermRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TermRepository termRepository;

    @Test
    public void whenSaveTerm_thenFindById() {
        DegreeMap dm = new DegreeMap("College");
        entityManager.persist(dm);
        Year year = new Year("2021-2022", dm); 
        entityManager.persist(year);
        Term term = new Term("Fall 2021", year);
        entityManager.persist(term);
        entityManager.flush();

        Term found = termRepository.findById(term.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getName(), term.getName());
        assertEquals(found.getYear().getName(), year.getName());
    }

    @Test
    public void whenDeleteTerm_thenNotFound() {
        DegreeMap dm = new DegreeMap("College");
        entityManager.persist(dm);
        Year year = new Year("2021-2022", dm);         entityManager.persist(year);
        Term term = new Term("Spring 2022", year);
        entityManager.persist(term);
        entityManager.flush();

        termRepository.delete(term);
        Term found = termRepository.findById(term.getId()).orElse(null);
        assertNull(found);
    }
}
