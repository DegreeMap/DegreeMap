package com.degreemap.DegreeMap.catalogs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogRepository;

@DataJpaTest
public class CatalogRepositoryTests {
    @Autowired
    private CourseCatalogRepository catalogRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void whenSaveCatalog_thenSuccess() {
        CourseCatalog catalog = new CourseCatalog("Computer Science");
        CourseCatalog savedCatalog = catalogRepository.save(catalog);
        assertEquals(entityManager.find(CourseCatalog.class, savedCatalog.getId()), catalog);
    }

    @Test
    void whenDuplicateName_thenSuccess() {
        CourseCatalog catalog1 = new CourseCatalog("Computer Science");
        CourseCatalog saved1 = catalogRepository.save(catalog1);
        CourseCatalog catalog2 = new CourseCatalog("Computer Science");
        CourseCatalog saved2 = catalogRepository.save(catalog2);
        assertEquals(entityManager.find(CourseCatalog.class, saved1.getId()), catalog1);
        assertEquals(entityManager.find(CourseCatalog.class, saved2.getId()), catalog2);

    }

    @Test
    void whenNullName_thenFailure() {
        assertThrows(IllegalArgumentException.class, () -> {
            CourseCatalog catalog = new CourseCatalog(null);
            catalogRepository.save(catalog);
        });
    }

    @Test
    void whenEmptyName_thenFailure() {
        assertThrows(IllegalArgumentException.class, () -> {
            CourseCatalog catalog = new CourseCatalog("");
            catalogRepository.save(catalog);
        });
    }
}
