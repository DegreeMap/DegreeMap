package com.degreemap.DegreeMap.dmEntities.blocks;

import com.degreemap.DegreeMap.dmEntities.terms.Term;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "blocks")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "termId", nullable = false)
    @JsonBackReference
    private Term term;

    public Block(){}

    public Block(String name, Term term){
        if(name == null || term == null){
            throw new IllegalArgumentException("All fields must be filled for DegreeMaps");
        }
        this.name = name;
        this.term = term;
        term.setBlock(this);
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

    public Term getTerm() {
        return term;
    }
    public void setTerm(Term term) {
        this.term = term;
    }    
}
