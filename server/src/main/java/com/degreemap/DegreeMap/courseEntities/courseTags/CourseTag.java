package com.degreemap.DegreeMap.courseEntities.courseTags;

import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.tags.Tag;

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

    @ManyToOne
    @JoinColumn(name = "tagId", nullable = false)
    private Tag tag;

    public CourseTag() {
    }

    public CourseTag(Course course, Tag tag) {
        if(course == null || tag == null){
            throw new IllegalArgumentException("All fields must be filled for CourseTags");
        }
        this.course = course;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
