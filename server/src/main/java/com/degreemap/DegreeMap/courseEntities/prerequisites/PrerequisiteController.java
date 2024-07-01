package com.degreemap.DegreeMap.courseEntities.prerequisites;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;

import java.util.List;

@RestController
@RequestMapping("/api/prerequisites")
public class PrerequisiteController {

    // TODO update returns to ResponseEntities


    @Autowired
    private PrerequisiteRepository prerequisiteRepository;
        @Autowired
    private CourseRepository courseRepository;

    @PostMapping
    public ResponseEntity<?> createPrerequisite(@RequestParam Long prereqCourseId, @RequestParam Long connectedCourseId, @RequestParam GradeRequirement gradeRequirement) {
        if(prereqCourseId == null || connectedCourseId == null || gradeRequirement == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Missing fields for Prerequisite data");
        }
        
        try {
            Course prereqCourse = courseRepository.findById(prereqCourseId)
                .orElseThrow(() -> new RuntimeException("Prereq Course not found with id: " + prereqCourseId));
            Course connectedCourse = courseRepository.findById(connectedCourseId)
                .orElseThrow(() -> new RuntimeException("Connected Course not found with id: " + connectedCourseId));
    
            Prerequisite prereq = new Prerequisite(gradeRequirement, prereqCourse, connectedCourse);
            return ResponseEntity.ok().body(prerequisiteRepository.save(prereq));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Prerequisite>> getAllPrerequisites() {
        return ResponseEntity.ok().body(prerequisiteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPrerequisiteById(@PathVariable Long id) {
        try {
            return prerequisiteRepository.findById(id)
                    .map(course -> ResponseEntity.ok(course))
                    .orElseThrow(() -> new RuntimeException("Prerequisite not found with id " + id));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // We won't support editing prerequisites. There is literally no point to it. 
    //
    // @PutMapping("/{id}")
    // public Prerequisite updatePrerequisite(@PathVariable Long id, @RequestBody Prerequisite updatedPrerequisite) {
    //     return prerequisiteRepository.findById(id).map(prerequisite -> {
    //         prerequisite.setGradeRequirement(updatedPrerequisite.getGradeRequirement());
    //         prerequisite.setPrereqCourse(updatedPrerequisite.getPrereqCourse());
    //         prerequisite.setConnectedCourse(updatedPrerequisite.getConnectedCourse());
    //         return prerequisiteRepository.save(prerequisite);
    //     }).orElseThrow(() -> new RuntimeException("Prerequisite not found with id " + id));
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrerequisite(@PathVariable Long id) {
        try {
            Prerequisite prerequisite = prerequisiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prerequisite not found with id " + id));
            prerequisiteRepository.delete(prerequisite);
            prerequisiteRepository.save(prerequisite);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
