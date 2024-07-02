package com.degreemap.DegreeMap.courseEntities.courseTags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.courseEntities.tags.Tag;
import com.degreemap.DegreeMap.courseEntities.tags.TagRepository;

import java.util.List;

@RestController
@RequestMapping("/api/courseTags")
public class CourseTagController {

    static class Request {
        public Long courseId;
        public Long tagId;
    }

    @Autowired
    private CourseTagRepository courseTagRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TagRepository tagRepository;

    @PostMapping
    public ResponseEntity<?> createCourseTag(@RequestBody Request postRequest) {
        try {
            Course course = courseRepository.findById(postRequest.courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id " + postRequest.courseId));
            Tag tag = tagRepository.findById(postRequest.tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found with id " + postRequest.courseId));
            
            CourseTag courseTag = new CourseTag(course, tag);
            
            return ResponseEntity.ok(courseTagRepository.save(courseTag));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    @GetMapping
    public ResponseEntity<List<CourseTag>> getAllCourseTags() {
        return ResponseEntity.ok(courseTagRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseTagById(@PathVariable Long id) {
        try {
            return courseTagRepository.findById(id)
                    .map(courseTag -> ResponseEntity.ok(courseTag))
                    .orElseThrow(() -> new RuntimeException("CourseTag not found with id " + id));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    // Not supported, commented out here for later use if needed (still needs its returns to be updated and unit tests).
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
    public ResponseEntity<?> deleteCourseTag(@PathVariable Long id) {
        try {
            return courseTagRepository.findById(id).map(courseTag -> {
                courseTagRepository.delete(courseTag);
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new RuntimeException("CourseTag not found with id " + id));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
