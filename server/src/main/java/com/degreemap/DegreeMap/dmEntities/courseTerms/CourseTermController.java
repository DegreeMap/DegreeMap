package com.degreemap.DegreeMap.dmEntities.courseTerms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.dmEntities.terms.Term;
import com.degreemap.DegreeMap.dmEntities.terms.TermRepository;

@RestController
@RequestMapping("/api/courseTerms")
public class CourseTermController {
    static class Request {
        public Long courseId;
        public Long termId;
    }

    @Autowired
    public CourseTermRepository ctRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TermRepository termRepository;

    @PostMapping
    public ResponseEntity<?> createCourseTerm(@RequestBody Request postRequest) {
        try {
            Course course = courseRepository.findById(postRequest.courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + postRequest.courseId));
            Term term = termRepository.findById(postRequest.termId)
                .orElseThrow(() -> new RuntimeException("Term not found with id " + postRequest.termId));
            
            CourseTerm courseTerm = new CourseTerm(course, term);
            
            return ResponseEntity.ok(ctRepository.save(courseTerm));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    @GetMapping
    public ResponseEntity<List<CourseTerm>> getAllCourseTerms() {
        return ResponseEntity.ok(ctRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseTermById(@PathVariable Long id) {
        try {
            return ctRepository.findById(id)
                    .map(courseTerm -> ResponseEntity.ok(courseTerm))
                    .orElseThrow(() -> new RuntimeException("CourseTerm not found with id " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    // Not supported, commented out here for later use if needed (still needs its returns to be updated and unit tests).
    //
    // <this code was copied from course tag controller. :p
    //
    // @PutMapping("/{id}")
    // public CourseTag updateCourseTag(@PathVariable Long id, @RequestBody CourseTag updatedCourseTag) {
    //     return courseTagRepository.findById(id).map(courseTag -> {
    //         courseTag.setCourse(updatedCourseTag.getCourse());
    //         courseTag.setTag(updatedCourseTag.getTag());
    //         return courseTagRepository.save(courseTag);
    //     }).orElseThrow(() -> new RuntimeException("CourseTag not found with id " + id));
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourseTerm(@PathVariable Long id) {
        try {
            return ctRepository.findById(id).map(courseTerm -> {
                ctRepository.delete(courseTerm);
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new RuntimeException("CourseTerm not found with id " + id));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
