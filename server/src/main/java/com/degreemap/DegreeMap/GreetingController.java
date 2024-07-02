package com.degreemap.DegreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/greetings")
public class GreetingController {

    // TODO delete greetings

	// I provided a bunch of curl commands for you to run to test these out manually. 
	// You can run these in any command line after running both the docker database and the spring boot application theres instructions for those in the readME.

    @Autowired
    private GreetingRepository greetingRepository;

    // Create a new Greeting
	// curl -X POST -H "Content-Type: application/json" -d "{\"content\":\"your message here\"}" http://localhost:8080/api/greetings
	@PostMapping
	public Greeting createGreeting(@RequestBody Greeting greeting) {
		return greetingRepository.save(greeting);
	}
	

    // Retrieve all Greetings
	// curl -X GET http://localhost:8080/api/greetings
	// You can also just go to the url on a browser.
    @GetMapping
    public List<Greeting> getAllGreetings() {
        return greetingRepository.findAll();
    }

    // Retrieve a single Greeting by ID
	// curl -X GET http://localhost:8080/api/greetings/1
	// Replace '1' with the actual id 
	// You can also just go to the url on a browser.
    @GetMapping("/{id}")
    public Greeting getGreetingById(@PathVariable Long id) {
        return greetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greeting not found with id " + id));
    }

    // Update a Greeting
	// curl -X PUT -H "Content-Type: application/json" -d "{\"content\":\"Your message here.\"}" http://localhost:8080/api/greetings/1
    @PutMapping("/{id}")
    public Greeting updateGreeting(@PathVariable Long id, @RequestBody Greeting updatedGreeting) {
        return greetingRepository.findById(id).map(greeting -> {
            greeting.setContent(updatedGreeting.getContent());
            return greetingRepository.save(greeting);
        }).orElseThrow(() -> new RuntimeException("Greeting not found with id " + id));
    }

	// This is glitchy with the IDs. A problem not worth fixing since We won't have support for deleting courses. 

    // Delete a Greeting
	// curl -X DELETE http://localhost:8080/api/greetings/1
	// Replace '1' with the actual id 
    @DeleteMapping("/{id}")
    public void deleteGreeting(@PathVariable Long id) {
        Greeting greeting = greetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Greeting not found with id " + id));
        greetingRepository.delete(greeting);
    }
}
