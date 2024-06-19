package com.degreemap.DegreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/corequisites")
public class CorequisiteController {

    @Autowired
    private CorequisiteRepository corequisiteRepository;

    @PostMapping
    public Corequisite createCorequisite(@RequestBody Corequisite corequisite) {
        return corequisiteRepository.save(corequisite);
    }

    @GetMapping
    public List<Corequisite> getAllCorequisites() {
        return corequisiteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Corequisite getCorequisiteById(@PathVariable Long id) {
        return corequisiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Corequisite not found with id " + id));
    }

    @PutMapping("/{id}")
    public Corequisite updateCorequisite(@PathVariable Long Id, @RequestBody Corequisite updatedCorequisite) {
        return corequisiteRepository.findById(id).map(corequisite -> {
            corequisite.setCoreqCourse(updatedCorequisite.getCoreqCourse());
            corequisite.setConnectedCourse(updatedCorequisite.getConnectedCourse());
            return corequisiteRepository.save(corequisite);
        }).orElseThrow(() -> new RuntimeException("Corequisite not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteCorequisite(@PathVariable Long id) {
        Corequisite corequisite = corequisiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Corequisite not found with id " + id));
        corequisiteRepository.delete(corequisite);
    }
}
