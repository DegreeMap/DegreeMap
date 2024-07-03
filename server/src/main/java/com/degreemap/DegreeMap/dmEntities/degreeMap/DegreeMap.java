package com.degreemap.DegreeMap.dmEntities.degreeMap;

import jakarta.persistence.*;

@Entity
@Table(name = "degree_maps")
public class DegreeMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public DegreeMap() {}

    public DegreeMap(String name) {
        if(name == null){
            throw new IllegalArgumentException("All fields must be filled for DegreeMaps");
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
        this.name = name;
    }
}
