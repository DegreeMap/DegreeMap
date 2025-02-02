package com.degreemap.DegreeMap.courseEntities.corequisites;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.degreemap.DegreeMap.courseEntities.courses.Course;

public interface CorequisiteRepository extends JpaRepository<Corequisite, Long> { 
    @Query("SELECT c FROM Corequisite c WHERE c.coreqCourse = :connectedCourse AND c.connectedCourse = :coreqCourse")
    Optional<Corequisite> findReciprocal(Course coreqCourse, Course connectedCourse);
}