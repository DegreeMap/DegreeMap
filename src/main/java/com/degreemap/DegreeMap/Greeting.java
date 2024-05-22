package com.degreemap.DegreeMap;

import jakarta.persistence.*;

@Entity
public class Greeting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;

    public Greeting() {
        // JPA requires a no-arg constructor
    }

    public Greeting(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or blank");
        }
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


