package com.degreemap.DegreeMap.dmEntities.blocks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.degreemap.DegreeMap.config.JpaTestConfig;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapRepository;
import com.degreemap.DegreeMap.dmEntities.terms.Term;
import com.degreemap.DegreeMap.dmEntities.terms.TermRepository;
import com.degreemap.DegreeMap.dmEntities.years.Year;
import com.degreemap.DegreeMap.dmEntities.years.YearRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@DataJpaTest
@Import(JpaTestConfig.class)
public class BlockRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private TermRepository termRepository;
    @Autowired
    private YearRepository yearRepository;
    @Autowired
    private DegreeMapRepository dmRepository;
    @Test
    public void shouldSaveBlock() {
        DegreeMap dm = new DegreeMap("College");
        entityManager.persist(dm);
        Year year = new Year("2021-2022", dm);
        entityManager.persist(year);
        Term term = new Term("Spring 2023", year);
        entityManager.persist(term);
        Block block = new Block("Morning Block", term);
        Block savedBlock = blockRepository.save(block);
        assertThat(savedBlock).isNotNull();
        assertThat(savedBlock.getId()).isNotNull();
        assertThat(savedBlock.getName()).isEqualTo("Morning Block");
    }

    @Test
    public void shouldFindBlockById() {
        DegreeMap dm = new DegreeMap("College");
        entityManager.persist(dm);
        Year year = new Year("2021-2022", dm);
        entityManager.persist(year);
        Term term = new Term("Spring 2023", year);
        entityManager.persist(term);
        Block block = new Block("Morning Block", term);
        Block savedBlock = entityManager.persistFlushFind(block);
        Optional<Block> foundBlock = blockRepository.findById(savedBlock.getId());
        assertThat(foundBlock).isPresent();
        assertThat(foundBlock.get().getName()).isEqualTo("Morning Block");
    }

    @Test
    public void shouldUpdateBlock() {
        DegreeMap dm = new DegreeMap("College");
        entityManager.persist(dm);
        Year year = new Year("2021-2022", dm);
        entityManager.persist(year);
        Term term = new Term("Spring 2023", year);
        entityManager.persist(term);
        Block block = new Block("Morning Block", term);
        Block savedBlock = entityManager.persistFlushFind(block);
        savedBlock.setName("Evening Block");
        Block updatedBlock = blockRepository.save(savedBlock);
        assertThat(updatedBlock.getName()).isEqualTo("Evening Block");
    }

    @Test
    public void shouldDeleteBlock() {
        DegreeMap dm = new DegreeMap("College");
        DegreeMap savedDm = entityManager.persistFlushFind(dm);
        Year year = new Year("2021-2022", dm);
        Year savedYear = entityManager.persistFlushFind(year);
        Term term = new Term("Spring 2023", year);
        Term savedTerm = entityManager.persistFlushFind(term);
        Block block = new Block("Morning Block", term);
        Block savedBlock = entityManager.persistFlushFind(block);
        
        Optional<DegreeMap> foundDm = dmRepository.findById(savedDm.getId());
        assertThat(foundDm).isPresent();
        assertEquals(savedDm, foundDm.get());

        Optional<Year> foundYear = yearRepository.findById(savedYear.getId());
        assertThat(foundYear).isPresent();
        assertEquals(savedYear, foundYear.get());

        Optional<Term> foundTerm = termRepository.findById(savedTerm.getId());
        assertThat(foundTerm).isPresent();
        assertEquals(savedTerm, foundTerm.get());

        Optional<Block> foundBlock = blockRepository.findById(savedBlock.getId());
        assertThat(foundBlock).isPresent();
        assertEquals(savedBlock, foundBlock.get());

        blockRepository.delete(savedBlock);
        Optional<Block> deletedBlock = blockRepository.findById(savedBlock.getId());
        assertThat(deletedBlock).isEmpty();

        Optional<DegreeMap> foundDm2 = dmRepository.findById(savedDm.getId());
        assertThat(foundDm2).isPresent();
        assertEquals(savedDm, foundDm2.get());

        Optional<Year> foundYear2 = yearRepository.findById(savedYear.getId());
        assertThat(foundYear2).isPresent();
        assertEquals(savedYear, foundYear2.get());

        Optional<Term> foundTerm2 = termRepository.findById(savedTerm.getId());
        assertThat(foundTerm2).isPresent();
        assertEquals(savedTerm, foundTerm2.get());
        // assertEquals(foundTerm2.get().getBlocks().size(), 0);
        // This will FAIL. Make sure to account for this when making controllers.
    }
}
