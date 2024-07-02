package com.degreemap.DegreeMap.courseEntities.corequisites;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;

import java.util.List;

@RestController
@RequestMapping("/api/corequisites")
public class CorequisiteController {

    static class Request {
        public Long coreqCourseId;
        public Long connectedCourseId;
    }

    @Autowired
    private CorequisiteRepository corequisiteRepository;
    @Autowired
    private CourseRepository courseRepository;

    @PostMapping
    public ResponseEntity<?> createCorequisite(@RequestBody Request postRequest) {
        if(postRequest.coreqCourseId == null || postRequest.connectedCourseId == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing fields for Corequisite data");
        }
        try {
            Course coreqCourse = courseRepository.findById(postRequest.coreqCourseId)
                .orElseThrow(() -> new RuntimeException("Coreq Course not found with id: " + postRequest.coreqCourseId));
            Course connectedCourse = courseRepository.findById(postRequest.connectedCourseId)
                .orElseThrow(() -> new RuntimeException("Connected Course not found with id: " + postRequest.connectedCourseId));
    
            Corequisite coreq = new Corequisite(coreqCourse, connectedCourse);
            
            // Must save reciprocal relationship (GCIS-124 has SWEN-250 as a coreq, SWEN-250 has GCIS-124 as a coreq)
            Corequisite coreqReciprocal = new Corequisite(connectedCourse, coreqCourse);
            corequisiteRepository.save(coreqReciprocal);

            return ResponseEntity.ok().body(corequisiteRepository.save(coreq));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Corequisite>> getAllCorequisites() {
        return ResponseEntity.ok().body(corequisiteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCorequisiteById(@PathVariable Long id) {
        try {
            return corequisiteRepository.findById(id)
                    .map(course -> ResponseEntity.ok(course))
                    .orElseThrow(() -> new RuntimeException("Corequisite not found with id " + id));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // not supported... obviously
    //
    // @PutMapping("/{id}")
    // public Corequisite updateCorequisite(@PathVariable Long id, @RequestBody Corequisite updatedCorequisite) {
    //     return corequisiteRepository.findById(id).map(corequisite -> {
    //         corequisite.setCoreqCourse(updatedCorequisite.getCoreqCourse());
    //         corequisite.setConnectedCourse(updatedCorequisite.getConnectedCourse());
    //         return corequisiteRepository.save(corequisite);
    //     }).orElseThrow(() -> new RuntimeException("Corequisite not found with id " + id));
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCorequisite(@PathVariable Long id) {
        try {
            Corequisite corequisite = corequisiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Corequisite not found with id " + id));
            corequisiteRepository.delete(corequisite);
            corequisiteRepository.save(corequisite);

            Corequisite reciprocal = corequisiteRepository.findReciprocal(corequisite.getCoreqCourse(), corequisite.getConnectedCourse())
                .orElseThrow(() -> new RuntimeException("Corequisite Reciprocal not found with id " + id +". Must delete Corequisite manually in database."));
            corequisiteRepository.delete(reciprocal);
            corequisiteRepository.save(reciprocal);

            return ResponseEntity.ok().build();
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
