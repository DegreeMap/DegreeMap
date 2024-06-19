package com.degreemap.DegreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/degreeMaps")
public class DegreeMapController {

    @Autowired
    private DegreeMapRepository degreeMapRepository;

    @PostMapping
    public DegreeMap createDegreeMap(@RequestBody DegreeMap degreeMap) {
        return degreeMapRepository.save(degreeMap);
    }

    @GetMapping
    public List<DegreeMap> getAllDegreeMaps() {
        return degreeMapRepository.findAll();
    }

    @GetMapping("/{id}")
    public DegreeMap getDegreeMapById(@PathVariable Long id) {
        return degreeMapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DegreeMap not found with id " + id));
    }

    @PutMapping("/{id}")
    public DegreeMap updateDegreeMap(@PathVariable Long id, @RequestBody DegreeMap updatedDegreeMap) {
        return degreeMapRepository.findById(id).map(degreeMap -> {
            degreeMap.setName(updatedDegreeMap.getName());
            degreeMap.setYear(updatedDegreeMap.getYear());
            degreeMap.setTerm(updatedDegreeMap.getTerm());
            return degreeMapRepository.save(degreeMap);
        }).orElseThrow(() -> new RuntimeException("DegreeMap not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteDegreeMap(@PathVariable Long id) {
        DegreeMap degreeMap = degreeMapRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DegreeMap not found with id " + id));
        degreeMapRepository.delete(degreeMap);
    }
}
