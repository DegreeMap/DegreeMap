package com.degreemap.DegreeMap.courseEntities.tags;

import java.util.HashSet;
import java.util.Set;

import com.degreemap.DegreeMap.courseEntities.courseTags.CourseTag;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "tag")
    @JsonBackReference
    private Set<CourseTag> courseTags = new HashSet<>();

    public Tag() {
    }

    public Tag(String name) {
        if(name == null){
            throw new IllegalArgumentException("All fields must be filled for Corequisites");
        }
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

