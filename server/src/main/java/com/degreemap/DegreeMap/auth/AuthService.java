package com.degreemap.DegreeMap.auth;

import com.degreemap.DegreeMap.auth.jwt.AuthResponseDto;
import com.degreemap.DegreeMap.auth.jwt.JwtGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JpaUserDetailsService userDetailsService;
    private final JwtGenerator jwtGenerator;

    public AuthService(PasswordEncoder passwordEncoder, JpaUserDetailsService userDetailsService, JwtGenerator jwtGenerator) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtGenerator = jwtGenerator;
    }

    /**
     * Attempts to authenticate a user based on a given email and password.
     * @param email email to authenticate with
     * @param password password to authenticate with
     * @return the UserDetails of that user.
     * @throws ResponseStatusException in two cases: <ul>
     *     <li>
     *         If a user with the given email is not found, throws with a 404 NOT FOUND
     *     </li>
     *     <li>
     *         If a user with the given email is found, but the given password is incorrect,
     *         throws with a 401 UNAUTHORIZED
     *     </li>
     * </ul>
     */
    private UserDetails authenticateUser(String email, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Incorrect password");
            }

            return userDetails;

        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    public AuthResponseDto getAccessTokenFromCredentials(String email, String password) {

        UserDetails userDetails = authenticateUser(email, password);

        String accessToken = jwtGenerator.generateAccessToken(userDetails);

        return new AuthResponseDto(
                accessToken,
                15 * 60, // 15 mins in seconds
                                        // I'd like to standardize this value somewhere if possible/reasonable
                email
        );
    }
}
