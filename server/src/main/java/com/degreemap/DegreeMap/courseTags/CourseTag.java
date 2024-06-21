package com.degreemap.DegreeMap.courseTags;

import com.degreemap.DegreeMap.courses.Course;

import jakarta.persistence.*;

@Entity
@Table(name = "course_tags")
public class CourseTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;
    // TODO make course and tag foreign keys. should be ints i think?

    @ManyToOne
    @JoinColumn(name = "tagId", nullable = false)
    private Tag tag;

    public CourseTag() {
        // JPA requires a no-arg constructor
    }

    public CourseTag(Course course, Tag tag) {
        this.course = course;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
