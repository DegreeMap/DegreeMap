package com.degreemap.DegreeMap;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public record Greeting(@Id @GeneratedValue(strategy = GenerationType.AUTO) Long id, String content) {
    // Constructor with validation remains unchanged
    public Greeting {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or blank");
        }
    }
    // TODO: Handle dependencies. 
    // My goal: create a simple CRUD application to test if the docker server works. the tutorial i was using used weird dependencies that im not keen on using. you can just simply create one without depenencies if you want
}


