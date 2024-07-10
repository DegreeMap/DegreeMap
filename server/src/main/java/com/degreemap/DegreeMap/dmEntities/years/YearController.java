package com.degreemap.DegreeMap.dmEntities.years;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapRepository;

@RestController
@RequestMapping("/api/years")
public class YearController {
    static class Request {
        public String name;
        public Long degreeMapId;
    }
    
    @Autowired
    private YearRepository yearRepository;
    @Autowired
    private DegreeMapRepository degreeMapRepository;

    @PostMapping
    public ResponseEntity<?> createYear(@RequestBody Request postRequest) {
        try {
            DegreeMap degreeMap = degreeMapRepository.findById(postRequest.degreeMapId)
                .orElseThrow(() -> new RuntimeException("DegreeMap not found with id " + postRequest.degreeMapId));
            
            Year year = new Year(postRequest.name, degreeMap);
            degreeMap.getYears().add(year);
            degreeMapRepository.save(degreeMap);
            return ResponseEntity.ok(yearRepository.save(year));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @GetMapping
    public ResponseEntity<List<Year>> getAllYears() {
        return ResponseEntity.ok(yearRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getYearById(@PathVariable Long id) {
        try {
            return yearRepository.findById(id)
                    .map(year -> ResponseEntity.ok(year))
                    .orElseThrow(() -> new RuntimeException("Year not found with id " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateYear(@PathVariable Long id, @RequestBody Request postRequest) {
        try {
            return yearRepository.findById(id).map(year -> {
                year.setName(postRequest.name);
                return ResponseEntity.ok(yearRepository.save(year));
            }).orElseThrow(() -> new RuntimeException("Year not found with id " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteYear(@PathVariable Long id) {
        try {
            Year year = yearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Year not found with id " + id));
            DegreeMap degreeMap = year.getDegreeMap();
            yearRepository.delete(year);
            yearRepository.save(year);
            degreeMap.removeYear(year);
            degreeMapRepository.save(degreeMap);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }
}
