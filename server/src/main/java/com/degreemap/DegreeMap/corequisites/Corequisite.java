package com.degreemap.DegreeMap;
import jakarta.persistence.*;

@Entity
@Table(name = "corequisites")
public class Corequisite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coreqCourseId", nullable = false)
    private Course coreqCourse;

    @ManyToOne
    @JoinColumn(name = "connectedCourseId", nullable = false)
    private Course connectedCourse;

    public Corequisite() {
    }

    public Corequisite(Course coreqCourse, Course connectedCourse) {
        this.coreqCourse = coreqCourse;
        this.connectedCourse = connectedCourse;
    }

    public getId() {
        return id;
    }

    public getCoreqCourse() {
        return coreqCourse;
    }

    public getConnectedCourse() {
        return connectedCourse;
    }

    public void setCoreqCourse(Course coreqCourse) {
        this.coreqCourse = coreqCourse;
    }

    public void setConnectedCourse(Course connectedCourse) {
        this.connectedCourse = connectedCourse;
    }
}