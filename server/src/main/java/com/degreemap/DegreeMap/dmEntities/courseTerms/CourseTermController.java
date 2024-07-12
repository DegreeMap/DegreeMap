package com.degreemap.DegreeMap.dmEntities.courseTerms;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courseTerms")
public class CourseTermController {
    static class Request {
        public Long courseId;
        public Long termId;
    }
}
