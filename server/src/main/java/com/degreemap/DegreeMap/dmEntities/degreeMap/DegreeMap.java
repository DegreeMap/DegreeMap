package com.degreemap.DegreeMap.dmEntities.degreeMap;

import java.util.ArrayList;
import java.util.List;

import com.degreemap.DegreeMap.dmEntities.years.Year;
import com.degreemap.DegreeMap.users.userDm.UserDegreeMap;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "degree_maps")
public class DegreeMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy="degreeMap", cascade = CascadeType.ALL) // <-- cascadetype all means when you delete a CourseCatalog, it deletes all Courses related to it
    private List<Year> years = new ArrayList<Year>();

    @OneToMany(mappedBy = "degreeMap", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<UserDegreeMap> userDegreeMaps = new ArrayList<UserDegreeMap>();

    public DegreeMap() {}

    public DegreeMap(String name) {
        if(name == null){
            throw new IllegalArgumentException("All fields must be filled for DegreeMaps");
        }
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void addYear(Year year){
        this.years.add(year);
    }
    public void removeYear(Year year){
        this.years.remove(year);
    }
    public List<Year> getYears(){
        return this.years;
    }

    public List<UserDegreeMap> getUserDegreeMaps(){
        return this.userDegreeMaps;
    }
    public void addUserDM(UserDegreeMap userDM){
        this.userDegreeMaps.add(userDM);
    }
    public void removeUserDM(UserDegreeMap userDM){
        this.userDegreeMaps.remove(userDM);
    }
}
