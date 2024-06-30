package com.degreemap.DegreeMap.courseEntities.tags;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagRepository tagRepository; 
    
    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody Tag tag) {
        try {
            Tag createdTag = tagRepository.save(tag);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to create Tag");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTagById(@PathVariable Long id) {
        try {
            return tagRepository.findById(id)
                .map(tag -> ResponseEntity.ok(tag))
                .orElseThrow(() -> new RuntimeException("Tag with id " + id + " not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(tags);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable Long id, @RequestBody Tag updatedTag) {
        try {
            return tagRepository.findById(id).map(tag -> {
                tag.setName(updatedTag.getName());
                tagRepository.save(tag);
                return ResponseEntity.ok(tag);
            }).orElseThrow(() -> new RuntimeException("Tag with id " + id + " not found"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id){
        try {
            return tagRepository.findById(id).map(tag -> {
                tagRepository.delete(tag);
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new RuntimeException("Tag with id " + id + " not found"));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

}