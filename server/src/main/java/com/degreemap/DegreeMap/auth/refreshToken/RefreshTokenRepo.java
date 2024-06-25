package com.degreemap.DegreeMap.auth.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    /**
     * @return The latest refresh token;
     */
    @Query(
            value = """
            SELECT * FROM refresh_tokens
            WHERE user_id = :userId
            ORDER BY created_at DESC LIMIT 1
            """, nativeQuery = true
    )
    Optional<RefreshTokenEntity> findLatestRefreshTokenByUser(Long userId);
}
