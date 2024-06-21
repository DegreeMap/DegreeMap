package com.degreemap.DegreeMap.courseTags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courseTags")
public class CourseTagController {

    @Autowired
    private CourseTagRepository courseTagRepository;

    @PostMapping
    public CourseTag createCourseTag(@RequestBody CourseTag courseTag) {
        return courseTagRepository.save(courseTag);
    }

    @GetMapping
    public List<CourseTag> getAllCourseTags() {
        return courseTagRepository.findAll();
    }

    @GetMapping("/{id}")
    public CourseTag getCourseTagById(@PathVariable Long id) {
        return courseTagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CourseTag not found with id " + id));
    }

    @PutMapping("/{id}")
    public CourseTag updateCourseTag(@PathVariable Long id, @RequestBody CourseTag updatedCourseTag) {
        return courseTagRepository.findById(id).map(courseTag -> {
            courseTag.setCourse(updatedCourseTag.getCourse());
            courseTag.setTag(updatedCourseTag.getTag());
            return courseTagRepository.save(courseTag);
        }).orElseThrow(() -> new RuntimeException("CourseTag not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteCourseTag(@PathVariable Long id) {
        CourseTag courseTag = courseTagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CourseTag not found with id " + id));
        courseTagRepository.delete(courseTag);
    }
}
