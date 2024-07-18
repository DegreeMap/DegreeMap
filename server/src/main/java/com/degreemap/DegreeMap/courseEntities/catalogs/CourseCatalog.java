package com.degreemap.DegreeMap.courseEntities.catalogs;
import java.util.ArrayList;
import java.util.List;

import com.degreemap.DegreeMap.courseEntities.courses.Course;

import jakarta.persistence.*;

@Entity
@Table(name = "course_catalogs")
public class CourseCatalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy="courseCatalog", cascade = CascadeType.ALL) // <-- cascadetype all means when you delete a CourseCatalog, it deletes all Courses related to it
    private List<Course> courses = new ArrayList<Course>();

    public CourseCatalog() {
    }

    public CourseCatalog(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
    }

    public void addCourse(Course course){
        this.courses.add(course);
    }
    public void removeCourse(Course course){
        this.courses.remove(course);
    }
    public List<Course> getCourses(){
        return this.courses;
    }
}
