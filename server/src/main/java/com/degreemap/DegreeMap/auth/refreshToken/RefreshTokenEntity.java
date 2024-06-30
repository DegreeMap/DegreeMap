package com.degreemap.DegreeMap.auth.refreshToken;

import com.degreemap.DegreeMap.users.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "refresh_token", nullable = false, length = 10000)
    private String refreshToken;

    @Column
    private boolean revoked;

    // One user may have many refresh tokens.
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    public RefreshTokenEntity() {
    }

    public RefreshTokenEntity(String refreshToken, boolean revoked, User user) {
        this.refreshToken = refreshToken;
        this.revoked = revoked;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public User getUser() {
        return user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
