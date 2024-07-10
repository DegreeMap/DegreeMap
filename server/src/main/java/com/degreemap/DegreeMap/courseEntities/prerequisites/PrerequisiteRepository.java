package com.degreemap.DegreeMap.courseEntities.prerequisites;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrerequisiteRepository extends JpaRepository<Prerequisite, Long> {
    Optional<Prerequisite> findByConnectedCourseId(Long id);
}
