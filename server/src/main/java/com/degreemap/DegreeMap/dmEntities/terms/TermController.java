package com.degreemap.DegreeMap.dmEntities.terms;

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

import com.degreemap.DegreeMap.dmEntities.years.Year;
import com.degreemap.DegreeMap.dmEntities.years.YearRepository;

@RestController
@RequestMapping("/api/terms")
public class TermController {
    static class Request {
        public String name;
        public Long yearId;
    }

    @Autowired
    private TermRepository termRepository;
    @Autowired
    private YearRepository yearRepository;

    @PostMapping
    public ResponseEntity<?> createTerm(@RequestBody Request postRequest) {
        try {
            Year year = yearRepository.findById(postRequest.yearId)
                .orElseThrow(() -> new RuntimeException("Year not found with id " + postRequest.yearId));
            
            Term term = new Term(postRequest.name, year);
            year.addTerm(term);
            yearRepository.save(year);
            return ResponseEntity.ok(termRepository.save(term));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @GetMapping
    public ResponseEntity<List<Term>> getAllTerms() {
        return ResponseEntity.ok(termRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTermById(@PathVariable Long id) {
        try {
            return termRepository.findById(id)
                    .map(term -> ResponseEntity.ok(term))
                    .orElseThrow(() -> new RuntimeException("Term not found with id " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTerm(@PathVariable Long id, @RequestBody Request postRequest) {
        try {
            return termRepository.findById(id).map(term -> {
                term.setName(postRequest.name);
                return ResponseEntity.ok(termRepository.save(term));
            }).orElseThrow(() -> new RuntimeException("Term not found with id " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTerm(@PathVariable Long id) {
        try {
            Term term = termRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Term not found with id " + id));
            Year year = term.getYear();
            termRepository.delete(term);
            termRepository.save(term);
            year.removeTerm(term);
            yearRepository.save(year);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }
}
