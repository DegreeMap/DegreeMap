package com.degreemap.DegreeMap.users.userCatalog;

import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.users.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "user_course_catalogs")
public class UserCourseCatalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonBackReference
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "courseCatalogId", nullable = false)
    private CourseCatalog courseCatalog;

    public UserCourseCatalog() { }

    public UserCourseCatalog(User user, CourseCatalog catalog){
        if(user == null || catalog == null){
            throw new IllegalArgumentException("All fields must be filled for UserCourseCatalog");   
        }
        this.user = user;
        this.courseCatalog = catalog;
        
        this.user.addUserCC(this);
        this.courseCatalog.addUserCC(this);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public CourseCatalog getCourseCatalog() {
        return courseCatalog;
    }
    public void setCourseCatalog(CourseCatalog catalog) {
        this.courseCatalog = catalog;
    }
}