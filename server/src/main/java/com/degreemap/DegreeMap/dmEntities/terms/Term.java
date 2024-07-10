package com.degreemap.DegreeMap.dmEntities.terms;

import java.util.ArrayList;
import java.util.List;

import com.degreemap.DegreeMap.dmEntities.blocks.Block;
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

    @OneToMany(mappedBy="term", cascade = CascadeType.ALL) // <-- cascadetype all means when you delete a CourseCatalog, it deletes all Courses related to it
    private List<Block> blocks = new ArrayList<Block>();
    // TODO There can only be one block. I'm in a rush so I don't have time to research if there's a way to only have one associated.
    // research it later :/ (im guessing @OneToOne)

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

    public List<Block> getBlocks() {
        return blocks;
    }
    public void addBlock(Block block) {
        // TODO: Fix this horrid code
        
        List<Block> newBlocks = new ArrayList<>();
        newBlocks.add(block);

        this.blocks = newBlocks;
    }

    public void removeBlock(Block block) {
        this.blocks.remove(block);
    }
}
