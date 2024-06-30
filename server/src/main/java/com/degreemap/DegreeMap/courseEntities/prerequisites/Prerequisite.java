package com.degreemap.DegreeMap.courseEntities.prerequisites;
import com.degreemap.DegreeMap.courseEntities.courses.Course;

import jakarta.persistence.*;

@Entity
@Table(name = "prerequisites")
public class Prerequisite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GradeRequirement gradeRequirement;

    @ManyToOne
    @JoinColumn(name = "prereqCourseId", nullable = false)
    private Course prereqCourse;

    @ManyToOne
    @JoinColumn(name = "connectedCourseId", nullable = false)
    private Course connectedCourse;

    // connectedCourse has prereqCourse as a prereq
    // GCIS-124 has GCIS-123 as a prereq

    public Prerequisite() {
    }

    public Prerequisite(GradeRequirement gradeRequirement, Course prereqCourse, Course connectedCourse) {
        if(gradeRequirement == null || prereqCourse == null || connectedCourse == null){
            throw new IllegalArgumentException("All fields must be filled for Prerequisites");
        }
        
        this.gradeRequirement = gradeRequirement;
        this.prereqCourse = prereqCourse;
        this.connectedCourse = connectedCourse;
    }

    public Long getId() {
        return id;
    }

    public GradeRequirement getGradeRequirement() {
        return gradeRequirement;
    }

    public void setGradeRequirement(GradeRequirement gradeRequirement) {
        this.gradeRequirement = gradeRequirement;
    }

    public Course getPrereqCourse() {
        return prereqCourse;
    }

    public void setPrereqCourse(Course prereqCourse) {
        this.prereqCourse = prereqCourse;
    }

    public Course getConnectedCourse() {
        return connectedCourse;
    }

    public void setConnectedCourse(Course connectedCourse) {
        this.connectedCourse = connectedCourse;
    }
}
