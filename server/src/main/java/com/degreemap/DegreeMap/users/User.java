package com.degreemap.DegreeMap.users;

import com.degreemap.DegreeMap.auth.refreshToken.RefreshTokenEntity;
import com.degreemap.DegreeMap.users.userCatalog.UserCourseCatalog;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // actually storing hash

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    // Lazy fetching means we only get refresh tokens on demand/when needed; they don't come along by default.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshTokenEntity> refreshTokens;

    @OneToMany(mappedBy = "courseCatalog", cascade = CascadeType.ALL)
    private List<UserCourseCatalog> userCourseCatalogs = new ArrayList<UserCourseCatalog>();

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RefreshTokenEntity> getRefreshTokens() {
        return refreshTokens;
    }

    public void setRefreshTokens(List<RefreshTokenEntity> refreshTokens) {
        this.refreshTokens = refreshTokens;
    }

    public List<UserCourseCatalog> getUserCCs(){
        return this.userCourseCatalogs;
    }
    public void addUserCC(UserCourseCatalog userCC){
        this.userCourseCatalogs.add(userCC);
    }
    public void removeCourseTerm(UserCourseCatalog userCC){
        this.userCourseCatalogs.remove(userCC);
    }
}
