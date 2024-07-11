package com.degreemap.DegreeMap.dmEntities.blocks;

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

import com.degreemap.DegreeMap.dmEntities.terms.Term;
import com.degreemap.DegreeMap.dmEntities.terms.TermRepository;

@RestController
@RequestMapping("/api/blocks")
public class BlockController {
    static class Request {
        public String name;
        public Long termId;
    }

    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private TermRepository termRepository;

    @PostMapping
    public ResponseEntity<?> createBlock(@RequestBody Request postRequest) {
        try {
            Term term = termRepository.findById(postRequest.termId)
                .orElseThrow(() -> new RuntimeException("Term not found with id " + postRequest.termId));
            // TODO add logic to make sure there aren't courses tied to the term (throw runtime)

            Block block = new Block(postRequest.name, term);
            term.addBlock(block);
            termRepository.save(term);
            return ResponseEntity.ok(blockRepository.save(block));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @GetMapping
    public ResponseEntity<List<Block>> getAllBlocks() {
        return ResponseEntity.ok(blockRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlockById(@PathVariable Long id) {
        try {
            return blockRepository.findById(id)
                    .map(block -> ResponseEntity.ok(block))
                    .orElseThrow(() -> new RuntimeException("Block not found with id " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlock(@PathVariable Long id, @RequestBody Request postRequest) {
        try {
            return blockRepository.findById(id).map(block -> {
                block.setName(postRequest.name);
                return ResponseEntity.ok(blockRepository.save(block));
            }).orElseThrow(() -> new RuntimeException("Block not found with id " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlock(@PathVariable Long id) {
        try {
            Block block = blockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Block not found with id " + id));
            Term term = block.getTerm();
            term.removeBlock(block);
            blockRepository.delete(block);
            blockRepository.save(block);
            termRepository.save(term);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }    
    }
}
