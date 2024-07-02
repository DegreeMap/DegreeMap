package com.degreemap.DegreeMap.courseEntities.corequisites;
import com.degreemap.DegreeMap.courseEntities.courses.Course;

import jakarta.persistence.*;

@Entity
@Table(name = "corequisites")
public class Corequisite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coreqCourseId", nullable = false)
    private Course coreqCourse;

    @ManyToOne
    @JoinColumn(name = "connectedCourseId", nullable = false)
    private Course connectedCourse;

    // connectedCourse has coreqCourse as a coreq
    // GCIS-124 has SWEN-250 as a coreq
    // SWEN-250 has GCIS-124 as a coreq

    public Corequisite() {
    }

    public Corequisite(Course coreqCourse, Course connectedCourse) {
        this.coreqCourse = coreqCourse;
        this.connectedCourse = connectedCourse;
    }

    public Long getId() {
        return id;
    }

    public Course getCoreqCourse() {
        return coreqCourse;
    }

    public Course getConnectedCourse() {
        return connectedCourse;
    }

    public void setCoreqCourse(Course coreqCourse) {
        this.coreqCourse = coreqCourse;
    }

    public void setConnectedCourse(Course connectedCourse) {
        this.connectedCourse = connectedCourse;
    }
}