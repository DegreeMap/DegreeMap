package com.degreemap.DegreeMap.dmEntities.years;

import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "years")
public class Year {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "degreeMapId", nullable = false)
    @JsonBackReference
    private DegreeMap degreeMap;

    public Year() { }

    public Year(String name, DegreeMap degreeMap){
        if(name == null || degreeMap == null){
            throw new IllegalArgumentException("All fields must be filled for Year");
        }
        this.name = name;
        this.degreeMap = degreeMap;
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

    public DegreeMap getDegreeMap() {
        return degreeMap;
    }
    public void setDegreeMap(DegreeMap degreeMap) {
        this.degreeMap = degreeMap;
    }    
}
