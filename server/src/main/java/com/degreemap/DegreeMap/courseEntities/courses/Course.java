package com.degreemap.DegreeMap.courseEntities.courses;

import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courseCatalogId", nullable = false)
    private CourseCatalog courseCatalog;
    // TODO make this a foreign key. Should be an int i think?

    @Column(nullable = false)
    private String name;

    @Column(name = "course_code", length = 10, nullable = false)
    private String courseCode = "";

    @Column(nullable = false)
    private int credits = 0;

    @Column(nullable = false)
    private String institution = "";

    @Column(nullable = false)
    private String college = "";

    @Column(nullable = false)
    private String department = "";

    public Course() {
    }

    public Course(CourseCatalog courseCatalog, String name, String courseCode, int credits, String institution, String college, String department) {
        this.courseCatalog = courseCatalog;
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
        this.courseCode = courseCode;
        this.credits = credits;
        this.institution = institution;
        this.college = college;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public CourseCatalog getCourseCatalog() {
        return courseCatalog;
    }

    public void setCourseCatalog(CourseCatalog courseCatalog) {
        this.courseCatalog = courseCatalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
