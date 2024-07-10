package com.degreemap.DegreeMap.dmEntities.terms;

import com.degreemap.DegreeMap.dmEntities.years.Year;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "terms")
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "yearId", nullable = false)
    @JsonBackReference
    private Year year;

    public Term() { }

    public Term(String name, Year year){
        if(name == null || year == null){
            throw new IllegalArgumentException("All fields must be filled for Term");   
        }
        this.name = name;
        this.year = year;
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

    public Year getYear() {
        return year;
    }
    public void setYear(Year year) {
        this.year = year;
    }
}
