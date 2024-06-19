package com.degreemap.DegreeMap;
import jakarta.persistence.*;

@Entity
@Table(name = "prerequisites")
public class Prerequisite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public Prerequisite() {
    }

    public Prerequisite(GradeRequirement gradeRequirement, Course prereqCourse, Course connectedCourse) {
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

    public enum GradeRequirement {
        S, A_PLUS, A, A_MINUS, B_PLUS, B, B_MINUS, C_PLUS, C, C_MINUS, D_PLUS, D, D_MINUS, F, NONE
    }
}
