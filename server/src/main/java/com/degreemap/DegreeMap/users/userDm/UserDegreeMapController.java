package com.degreemap.DegreeMap.users.userDm;

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

import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapRepository;
import com.degreemap.DegreeMap.users.User;
import com.degreemap.DegreeMap.users.UserRepository;

@RestController
@RequestMapping("/api/userDegreeMap")
public class UserDegreeMapController {
    static class Request {
        public Long userId;
        public Long degreeMapId;
    }

    @Autowired
    public UserDegreeMapRepository userDmRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DegreeMapRepository dmRepository;

    @PostMapping
    public ResponseEntity<?> createUserDM(@RequestBody Request postRequest) {
        try {
            User user = userRepository.findById(postRequest.userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + postRequest.userId));
            DegreeMap dm = dmRepository.findById(postRequest.degreeMapId)
                .orElseThrow(() -> new RuntimeException("DegreeMap not found with id " + postRequest.degreeMapId));
            
            UserDegreeMap userDm = new UserDegreeMap(user, dm);
            
            user.addUserDM(userDm);
            dm.addUserDM(userDm);
            userRepository.save(user);
            dmRepository.save(dm);
            return ResponseEntity.ok(userDmRepository.save(userDm));
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDegreeMap>> getAllUserDMs() {
        return ResponseEntity.ok(userDmRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserDMById(@PathVariable Long id) {
        try {
            return userDmRepository.findById(id)
                    .map(userDM -> ResponseEntity.ok(userDM))
                    .orElseThrow(() -> new RuntimeException("UserDegreeMap not found with id " + id));
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
            return userDmRepository.findById(id).map(userDM -> {
                userDmRepository.delete(userDM);
                User user = userDM.getUser();
                DegreeMap dm = userDM.getDegreeMap(); 

                user.removeUserDM(userDM);
                dm.removeUserDM(userDM);
                userRepository.save(user);
                dmRepository.save(dm);

                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new RuntimeException("UserDegreeMap not found with id " + id));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}