package com.degreemap.DegreeMap.users.userDm;

import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.users.User;

import jakarta.persistence.*;

@Entity
@Table(name = "user_degree_maps")
public class UserDegreeMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "degreeMapId", nullable = false)
    private DegreeMap degreeMap;

    public UserDegreeMap() { }

    public UserDegreeMap(User user, DegreeMap degreeMap){
        if(user == null || degreeMap == null){
            throw new IllegalArgumentException("All fields must be filled for UserDegreeMap");   
        }
        this.user = user;
        this.degreeMap = degreeMap;
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

    public DegreeMap getDegreeMap() {
        return degreeMap;
    }
    public void setDegreeMap(DegreeMap degreeMap) {
        this.degreeMap = degreeMap;
    }
}