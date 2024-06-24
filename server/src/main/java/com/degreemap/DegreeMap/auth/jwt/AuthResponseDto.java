package com.degreemap.DegreeMap.auth.jwt;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the access token (JWT)
 */
public record AuthResponseDto(
        @JsonProperty
        String accessToken,

        @JsonProperty
        int accessTokenExpiry,

        @JsonProperty
        String email
) { }
