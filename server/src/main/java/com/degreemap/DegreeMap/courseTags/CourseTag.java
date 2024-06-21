package com.degreemap.DegreeMap.courseTags;

import com.degreemap.DegreeMap.courses.Course;
import com.degreemap.DegreeMap.tags.Tag;

import jakarta.persistence.*;

@Entity
@Table(name = "course_tags")
public class CourseTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "tagId", nullable = false)
    private Tag tag;

    public CourseTag() {
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
