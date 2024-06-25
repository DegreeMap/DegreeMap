package com.degreemap.DegreeMap.auth;

import com.degreemap.DegreeMap.auth.jwt.AuthResponseDto;
import com.degreemap.DegreeMap.auth.jwt.JwtGenerator;
import com.degreemap.DegreeMap.users.User;
import com.degreemap.DegreeMap.users.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    private final UserRepository userRepository;

    public AuthService(PasswordEncoder passwordEncoder, JpaUserDetailsService userDetailsService, JwtGenerator jwtGenerator, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
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

    /**
     * Attempts to register a new user with the given email and password.
     * @param email email to register with
     * @param password password to register with
     * @return the UserDetails of the new user.
     * @throws ResponseStatusException with a 409 CONFLICT if a user with the given email already exists.
     */
    private UserDetails registerUser(String email, String password) {
        try {
            userDetailsService.loadUserByUsername(email);
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Registration failed: Duplicate email '"+email+"'. Please use a different email.");

        } catch (UsernameNotFoundException e) {
            // User doesn't exist, so we register a new one
            User user = new User(
                    email,
                    passwordEncoder.encode(password)
            );
            userRepository.save(user);
            return userDetailsService.loadUserByUsername(email);
        }
    }

    private AuthResponseDto makeAccessTokenResponse(UserDetails userDetails) {
        String accessToken = jwtGenerator.generateAccessToken(userDetails);

        return new AuthResponseDto(
                accessToken,
                15 * 60, // 15 mins
                userDetails.getUsername()
        );
    }

    public AuthResponseDto getAccessTokenFromCredentials(String email, String password) {

        UserDetails userDetails = authenticateUser(email, password);

        return makeAccessTokenResponse(userDetails);
    }

    public AuthResponseDto registerUserAndGetAccessToken(String email, String password) {

        UserDetails userDetails = registerUser(email, password);

        return makeAccessTokenResponse(userDetails);
    }
}
