package com.degreemap.DegreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/greetings")
public class GreetingController {

    @Autowired
    private GreetingRepository greetingRepository;

    // Create a new Greeting
	@PostMapping
	public Greeting createGreeting(@RequestBody Greeting greeting) {
		return greetingRepository.save(greeting);
	}
	

    // Retrieve all Greetings
    @GetMapping
    public List<Greeting> getAllGreetings() {
        return greetingRepository.findAll();
    }

    // Retrieve a single Greeting by ID
    @GetMapping("/{id}")
    public Greeting getGreetingById(@PathVariable Long id) {
        return greetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greeting not found with id " + id));
    }

    // Update a Greeting
    @PutMapping("/{id}")
    public Greeting updateGreeting(@PathVariable Long id, @RequestBody Greeting updatedGreeting) {
        return greetingRepository.findById(id).map(greeting -> {
            greeting.setContent(updatedGreeting.getContent());
            return greetingRepository.save(greeting);
        }).orElseThrow(() -> new RuntimeException("Greeting not found with id " + id));
    }

    // Delete a Greeting
    @DeleteMapping("/{id}")
    public void deleteGreeting(@PathVariable Long id) {
        Greeting greeting = greetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greeting not found with id " + id));
        greetingRepository.delete(greeting);
    }
}
