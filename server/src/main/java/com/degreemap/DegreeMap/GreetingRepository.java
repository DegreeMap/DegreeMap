package com.degreemap.DegreeMap;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GreetingRepository extends JpaRepository<Greeting, Long> { 
    // TODO delete greetings
}
