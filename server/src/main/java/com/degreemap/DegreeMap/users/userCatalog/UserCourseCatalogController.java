package com.degreemap.DegreeMap.users.userCatalog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogRepository;
import com.degreemap.DegreeMap.users.User;
import com.degreemap.DegreeMap.users.UserRepository;

@RestController
@RequestMapping("/api/courseTerms")
public class UserCourseCatalogController {
    static class Request {
        public Long userId;
        public Long catalogId;
    }

    @Autowired
    public UserCourseCatalogRepository userCcRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseCatalogRepository ccRepository;

    @PostMapping
    public ResponseEntity<?> createUserCC(@RequestBody Request postRequest) {
        try {
            User user = userRepository.findById(postRequest.userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + postRequest.userId));
            CourseCatalog cc = ccRepository.findById(postRequest.catalogId)
                .orElseThrow(() -> new RuntimeException("Catalog not found with id " + postRequest.catalogId));
            
            UserCourseCatalog userCC = new UserCourseCatalog(user, cc);
            
            user.addUserCC(userCC);
            cc.addUserCC(userCC);
            userRepository.save(user);
            ccRepository.save(cc);
            return ResponseEntity.ok(userCcRepository.save(userCC));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    @GetMapping
    public ResponseEntity<List<UserCourseCatalog>> getAllUserCCs() {
        return ResponseEntity.ok(userCcRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserCCById(@PathVariable Long id) {
        try {
            return userCcRepository.findById(id)
                    .map(userCC -> ResponseEntity.ok(userCC))
                    .orElseThrow(() -> new RuntimeException("UserCourseCatalog not found with id " + id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    // Not supported, commented out here for later use if needed (still needs its returns to be updated and unit tests).
    //
    // <this code was copied from course tag controller. :p
    //
    // @PutMapping("/{id}")
    // public CourseTag updateCourseTag(@PathVariable Long id, @RequestBody CourseTag updatedCourseTag) {
    //     return courseTagRepository.findById(id).map(courseTag -> {
    //         courseTag.setCourse(updatedCourseTag.getCourse());
    //         courseTag.setTag(updatedCourseTag.getTag());
    //         return courseTagRepository.save(courseTag);
    //     }).orElseThrow(() -> new RuntimeException("CourseTag not found with id " + id));
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserCC(@PathVariable Long id) {
        try {
            return userCcRepository.findById(id).map(userCC -> {
                userCcRepository.delete(userCC);
                User user = userCC.getUser();
                CourseCatalog cc = userCC.getCourseCatalog(); 

                user.removeUserCC(userCC);
                cc.removeUserCC(userCC);
                userRepository.save(user);
                ccRepository.save(cc);
                
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new RuntimeException("UserCourseCatalog not found with id " + id));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}