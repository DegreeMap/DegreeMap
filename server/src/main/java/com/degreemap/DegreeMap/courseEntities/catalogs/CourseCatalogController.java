package com.degreemap.DegreeMap.courseEntities.catalogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/course-catalogs")
public class CourseCatalogController {

    /*
     * Format for receiving requests from frontend.
     */
    static class Request {
        public String name;
    }

    @Autowired
    private CourseCatalogRepository courseCatalogRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> createCourseCatalog(@RequestBody Request postRequest) {
        try {
            CourseCatalog savedCatalog = courseCatalogRepository.save(new CourseCatalog(postRequest.name));
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCatalog);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<CourseCatalog>> getAllCourseCatalogs() {
       List<CourseCatalog> catalogs = courseCatalogRepository.findAll(); 
        return ResponseEntity.status(HttpStatus.OK).body(catalogs);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<?> getCourseCatalogById(@PathVariable Long id) {
        try {
            return courseCatalogRepository.findById(id)
                    .map(catalog -> ResponseEntity.ok(catalog))
                    .orElseThrow(() -> new RuntimeException("Catalog not found with id " + id));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateCourseCatalog(@PathVariable Long id, @RequestBody Request putRequest) {
        try {
            if(putRequest.name == null){
                throw new IllegalArgumentException("Missing field for Tag");
            }
            return courseCatalogRepository.findById(id).map(courseCatalog -> {
                courseCatalog.setName(putRequest.name);
                return ResponseEntity.ok(courseCatalogRepository.save(courseCatalog));
            }).orElseThrow(() -> new RuntimeException("CourseCatalog not found with id " + id));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }    

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteCourseCatalog(@PathVariable Long id) {
        try {
            return courseCatalogRepository.findById(id).map(catalog -> {
                courseCatalogRepository.delete(catalog);
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new RuntimeException("CourseCatalog not found with id " + id));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
