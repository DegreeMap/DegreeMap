package com.degreemap.DegreeMap.dmEntities.terms;

import java.util.ArrayList;
import java.util.List;

import com.degreemap.DegreeMap.dmEntities.blocks.Block;
import com.degreemap.DegreeMap.dmEntities.courseTerms.CourseTerm;
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

    @OneToOne(mappedBy="term", cascade = CascadeType.ALL) // <-- cascadetype all means when you delete a CourseCatalog, it deletes all Courses related to it
    private Block block;
    // TODO There can only be one block. I'm in a rush so I don't have time to research if there's a way to only have one associated.
    // research it later :/ (im guessing @OneToOne)

    @OneToMany(mappedBy = "term")
    private List<CourseTerm> courseTerms = new ArrayList<>();

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

    public Block getBlock() {
        return block;
    }
    public void setBlock(Block block) {
        if(this.courseTerms.isEmpty()){
            this.block = block;
        } else {
            throw new RuntimeException("Term must not have any Courses in order to assign a Block to it");   
        }
    }
    public void deleteBlock(){
        this.block = null;
    }

    public List<CourseTerm> getCourseTerms(){
        return this.courseTerms;
    }
    public void addCourseTerm(CourseTerm courseTerm){
        if(this.block != null){
            this.courseTerms.add(courseTerm);
        } else {
            throw new RuntimeException("Term must not be assigned a Block in order to have Courses");   
        }
    }
    public void removeCourseTerm(CourseTerm courseTerm){
        this.courseTerms.remove(courseTerm);
    }
}
