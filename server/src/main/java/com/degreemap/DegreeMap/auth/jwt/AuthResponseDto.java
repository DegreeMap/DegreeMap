package com.degreemap.DegreeMap.auth.jwt;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Represents the access token (JWT)
 */
public record AuthResponseDto(
        @JsonProperty
        String accessToken,

        @JsonProperty
        int accessTokenExpiry, // in seconds

        @JsonProperty
        Date expiresAt,

        @JsonProperty
        String email
) { }
