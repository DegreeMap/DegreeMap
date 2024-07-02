package com.degreemap.DegreeMap.courseEntities.tags;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.degreemap.DegreeMap.config.JpaTestConfig;

@DataJpaTest
@Import(JpaTestConfig.class)
public class TagRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TagRepository tagRepository;

    @Test
    public void whenSaveTag_thenFindById() {
        Tag tag = new Tag("Data Structures");
        entityManager.persist(tag);
        entityManager.flush();

        Tag found = tagRepository.findById(tag.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(found.getName(), tag.getName());
    }

    @Test
    public void whenSaveTag_NullFields_thenFailure() {
        assertThrows(IllegalArgumentException.class, () -> {
            Tag tag = new Tag(null);
            tagRepository.save(tag);
        });
    }

    @Test
    public void whenDeleteTag_thenNotFound() {
        Tag tag = new Tag("Computer Science");
        entityManager.persist(tag);
        entityManager.flush();

        tagRepository.delete(tag);

        Tag found = tagRepository.findById(tag.getId()).orElse(null);
        assertNull(found);
    }
}
