package com.degreemap.DegreeMap.catalogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/course-catalogs")
public class CourseCatalogController {

    @Autowired
    private CourseCatalogRepository courseCatalogRepository;

    @PostMapping
    public CourseCatalog createCourseCatalog(@RequestBody CourseCatalog courseCatalog) {
        return courseCatalogRepository.save(courseCatalog);
    }

    @GetMapping
    public List<CourseCatalog> getAllCourseCatalogs() {
        return courseCatalogRepository.findAll();
    }

    @GetMapping("/{id}")
    public CourseCatalog getCourseCatalogById(@PathVariable Long id) {
        return courseCatalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CourseCatalog not found with id " + id));
    }

    @PutMapping("/{id}")
    public CourseCatalog updateCourseCatalog(@PathVariable Long id, @RequestBody CourseCatalog updatedCourseCatalog) {
        return courseCatalogRepository.findById(id).map(courseCatalog -> {
            courseCatalog.setName(updatedCourseCatalog.getName());
            return courseCatalogRepository.save(courseCatalog);
        }).orElseThrow(() -> new RuntimeException("CourseCatalog not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteCourseCatalog(@PathVariable Long id) {
        CourseCatalog courseCatalog = courseCatalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CourseCatalog not found with id " + id));
        courseCatalogRepository.delete(courseCatalog);
    }
}
