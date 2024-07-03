package com.degreemap.DegreeMap.dmEntities.degreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/degreeMaps")
public class DegreeMapController {

    static class Request {
        public String name;
    }
    
    @Autowired
    private DegreeMapRepository degreeMapRepository;

    @PostMapping
    public ResponseEntity<?> createDegreeMap(@RequestBody Request postRequest) {
        try {
            DegreeMap degreeMap = new DegreeMap(postRequest.name);
            
            return ResponseEntity.ok(degreeMapRepository.save(degreeMap));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @GetMapping
    public ResponseEntity<List<DegreeMap>> getAllDegreeMaps() {
        return ResponseEntity.ok(degreeMapRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDegreeMapById(@PathVariable Long id) {
        try {
            return degreeMapRepository.findById(id)
                    .map(degreeMap -> ResponseEntity.ok(degreeMap))
                    .orElseThrow(() -> new RuntimeException("DegreeMap not found with id " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDegreeMap(@PathVariable Long id, @RequestBody Request postRequest) {
        try {
            return degreeMapRepository.findById(id).map(degreeMap -> {
                degreeMap.setName(postRequest.name);
                return ResponseEntity.ok(degreeMapRepository.save(degreeMap));
            }).orElseThrow(() -> new RuntimeException("DegreeMap not found with id " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDegreeMap(@PathVariable Long id) {
        try {
            DegreeMap degreeMap = degreeMapRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("DegreeMap not found with id " + id));
            degreeMapRepository.delete(degreeMap);
            degreeMapRepository.save(degreeMap);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }
}
