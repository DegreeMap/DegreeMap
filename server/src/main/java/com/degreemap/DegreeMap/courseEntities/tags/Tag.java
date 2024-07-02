package com.degreemap.DegreeMap.courseEntities.tags;

import jakarta.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

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

