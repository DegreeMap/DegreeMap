package com.degreemap.DegreeMap.prerequisites;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prerequisites")
public class PrerequisiteController {

    @Autowired
    private PrerequisiteRepository prerequisiteRepository;

    @PostMapping
    public Prerequisite createPrerequisite(@RequestBody Prerequisite prerequisite) {
        return prerequisiteRepository.save(prerequisite);
    }

    @GetMapping
    public List<Prerequisite> getAllPrerequisites() {
        return prerequisiteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Prerequisite getPrerequisiteById(@PathVariable Long id) {
        return prerequisiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prerequisite not found with id " + id));
    }

    @PutMapping("/{id}")
    public Prerequisite updatePrerequisite(@PathVariable Long id, @RequestBody Prerequisite updatedPrerequisite) {
        return prerequisiteRepository.findById(id).map(prerequisite -> {
            prerequisite.setGradeRequirement(updatedPrerequisite.getGradeRequirement());
            prerequisite.setPrereqCourse(updatedPrerequisite.getPrereqCourse());
            prerequisite.setConnectedCourse(updatedPrerequisite.getConnectedCourse());
            return prerequisiteRepository.save(prerequisite);
        }).orElseThrow(() -> new RuntimeException("Prerequisite not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public void deletePrerequisite(@PathVariable Long id) {
        Prerequisite prerequisite = prerequisiteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prerequisite not found with id " + id));
        prerequisiteRepository.delete(prerequisite);
    }
}
