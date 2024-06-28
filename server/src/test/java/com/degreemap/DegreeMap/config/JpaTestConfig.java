package com.degreemap.DegreeMap.config;

import com.degreemap.DegreeMap.auth.JpaUserDetailsService;
import com.degreemap.DegreeMap.auth.LogoutHandlerService;
import com.degreemap.DegreeMap.auth.jwt.JwtEncryption;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        SecurityConfig.class,
        JpaUserDetailsService.class,
        LogoutHandlerService.class,
        JwtEncryption.class
})
public class JpaTestConfig {
}
