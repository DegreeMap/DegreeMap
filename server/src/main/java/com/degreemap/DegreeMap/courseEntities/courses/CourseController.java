package com.degreemap.DegreeMap.courseEntities.courses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogRepository;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    /*
     * Format for receiving requests and sending responses to and from frontend.
     */
    static class Request {
        public String name;
        public long courseCatalogID;
        public String courseCode;
        public int credits;
        public String institution;
        public String college;
        public String department;
    }

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseCatalogRepository courseCatalogRepository;

    @PostMapping
    @Transactional // <-- Ensures either everything succeeds or nothing succeeds
    public ResponseEntity<?> createCourse(@RequestBody Request postRequest) {
        try {
            CourseCatalog catalog = courseCatalogRepository.findById(postRequest.courseCatalogID)
                .orElseThrow(() -> new RuntimeException("CourseCatalog not found with id: " + postRequest.courseCatalogID));
            Course course = new Course(catalog, 
                                postRequest.name, 
                                postRequest.courseCode, 
                                postRequest.credits, 
                                postRequest.institution, 
                                postRequest.college, 
                                postRequest.department);
            
            Course savedCourse = courseRepository.save(course);
            courseCatalogRepository.save(catalog);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    @GetMapping
    @Transactional
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseRepository.findAll(); 
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            return courseRepository.findById(id)
                    .map(course -> ResponseEntity.ok(course))
                    .orElseThrow(() -> new RuntimeException("Course not found with id " + id));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Request putRequest) {
        try {
            if(putRequest.name == null){
                throw new IllegalArgumentException("Missing field for Tag");
            }
            return courseRepository.findById(id).map(course -> {
                course.setName(putRequest.name);
                course.setCourseCode(putRequest.courseCode);
                course.setCredits(putRequest.credits);
                course.setInstitution(putRequest.institution);
                course.setCollege(putRequest.college);
                course.setDepartment(putRequest.department);                
                return ResponseEntity.ok(courseRepository.save(course));
            }).orElseThrow(() -> new RuntimeException("Course not found with id " + id));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            return courseRepository.findById(id).map(course -> {
                CourseCatalog catalog = course.getCourseCatalog();
                catalog.removeCourse(course);
                courseRepository.delete(course);
                courseCatalogRepository.save(catalog);
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new RuntimeException("Course not found with id " + id));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
