package com.degreemap.DegreeMap.degreeMap;

import jakarta.persistence.*;

@Entity
@Table(name = "degree_maps")
public class DegreeMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int year;
    private String term;

    public DegreeMap() {
        // JPA requires a no-arg constructor
    }

    public DegreeMap(String name, int year, String term) {
        this.name = name;
        this.year = year;
        this.term = term;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
