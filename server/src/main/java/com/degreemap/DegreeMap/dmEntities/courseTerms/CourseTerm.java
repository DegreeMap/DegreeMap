package com.degreemap.DegreeMap.dmEntities.courseTerms;

import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.dmEntities.terms.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="course_terms")
public class CourseTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "termId", nullable = false)
    private Term term;

    public CourseTerm() {}

    public CourseTerm(Course course, Term term){
        if(course == null || term == null){
            throw new IllegalArgumentException("All fields must be filled for CourseTerm");   
        }
        this.course = course;
        this.term = term;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

    public Term getTerm() {
        return term;
    }
    public void setTerm(Term term) {
        this.term = term;
    }
}
