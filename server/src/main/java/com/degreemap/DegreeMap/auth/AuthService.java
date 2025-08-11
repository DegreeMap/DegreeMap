package com.degreemap.DegreeMap.auth;

import com.degreemap.DegreeMap.auth.jwt.AuthResponseDto;
import com.degreemap.DegreeMap.auth.jwt.JwtGenerator;
import com.degreemap.DegreeMap.auth.refreshToken.RefreshTokenEntity;
import com.degreemap.DegreeMap.auth.refreshToken.RefreshTokenRepo;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogRepository;
import com.degreemap.DegreeMap.courseEntities.courses.Course;
import com.degreemap.DegreeMap.courseEntities.courses.CourseRepository;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapRepository;
import com.degreemap.DegreeMap.users.User;
import com.degreemap.DegreeMap.users.UserRepository;
import com.degreemap.DegreeMap.users.userCatalog.UserCourseCatalog;
import com.degreemap.DegreeMap.users.userCatalog.UserCourseCatalogRepository;
import com.degreemap.DegreeMap.users.userDm.UserDegreeMap;
import com.degreemap.DegreeMap.users.userDm.UserDegreeMapRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JpaUserDetailsService userDetailsService;
    private final JwtGenerator jwtGenerator;
    private final RefreshTokenRepo refreshTokenRepo;
    private final UserCourseCatalogRepository userCcRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseCatalogRepository ccRepository;
    private final DegreeMapRepository degreeMapRepository;
    private final UserDegreeMapRepository udmRepo;

    public AuthService(PasswordEncoder passwordEncoder, CourseRepository courseRepository, DegreeMapRepository dmRepository, UserDegreeMapRepository udmRepo, CourseCatalogRepository ccRepository, JpaUserDetailsService userDetailsService, JwtGenerator jwtGenerator, UserRepository userRepository, UserCourseCatalogRepository userCcRepository, RefreshTokenRepo refreshTokenRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
        this.userCcRepository = userCcRepository;
        this.ccRepository = ccRepository;
        this.refreshTokenRepo = refreshTokenRepo;
        this.degreeMapRepository = dmRepository;
        this.udmRepo = udmRepo;
        this.courseRepository= courseRepository;
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

            // Default CC
            CourseCatalog cc = new CourseCatalog("Custom Courses");
            ccRepository.save(cc);
            UserCourseCatalog ucc = new UserCourseCatalog(user, cc);
            user.addUserCC(ucc);
            cc.addUserCC(ucc);
            userRepository.save(user);

            Course chem = new Course(cc, "Chemistry 101", "CHEM-101", 4, "RIT", "College of Science", "Chemistry");
            Course cs = new Course(cc, "Computer Science 101", "CODE-101", 4, "RIT", "College of Information Sciences", "Computer Science");
            Course eng = new Course(cc, "Writing 101", "WRIT-101", 4, "RIT", "College of Language Arts", "English");

            // Default DM
            DegreeMap dm = new DegreeMap("DegreeMap 1");
            degreeMapRepository.save(dm);
            UserDegreeMap udm = new UserDegreeMap(user, dm);
            user.addUserDM(udm);
            dm.addUserDM(udm);
            userRepository.save(user);
            
            userRepository.save(user);
            userCcRepository.save(ucc);
            udmRepo.save(udm);
            courseRepository.save(chem);
            courseRepository.save(cs);
            courseRepository.save(eng);
            return userDetailsService.loadUserByUsername(email);
        }
    }

    private void addRefreshTokenCookieToResponse(String refreshToken,
                                                 HttpServletResponse response) {

        Cookie cookie = new Cookie("refreshToken", refreshToken);

        // Makes cookie inaccessible to client-side (java)scripts.
        // Helps mitigate certain attacks, like cross-site scripting (XSS)
        cookie.setHttpOnly(true);

        // Makes it so the cookie is only sent to the client if it's being
        // requested over a secure HTTPS connection.
        // Set to true in prod
        cookie.setSecure(false);

        // Cookie will last 15 days. Note that the refresh token is also set
        // to last 15 days; make sure they match.
        cookie.setMaxAge(15 * 24 * 60 * 60);

        cookie.setDomain("localhost");
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    private AuthResponseDto makeAccessTokenResponse(UserDetails userDetails) {
        String accessToken = jwtGenerator.generateAccessToken(userDetails);

        return new AuthResponseDto(
                accessToken,
                15 * 60, // 15 mins
                Date.from(new Date().toInstant().plusSeconds(7)),
                userDetails.getUsername()
        );
    }

    private String createAndSaveRefreshToken(UserDetails userDetails) {
        String refreshToken = jwtGenerator.generateRefreshToken(userDetails);

        userRepository.findByEmail(userDetails.getUsername()).ifPresent(userEntity -> {
            // revoke user's latest refresh token
            refreshTokenRepo.findLatestRefreshTokenByUser(userEntity.getId())
                    .ifPresent(token -> {
                        token.setRevoked(true);
                        refreshTokenRepo.save(token);
                    });

            // create new refresh token
            var refreshTokenEntity = new RefreshTokenEntity(
                    refreshToken,
                    false,
                    userEntity
            );

            // save it to DB
            refreshTokenRepo.save(refreshTokenEntity);
        });

        return refreshToken;
    }

    public AuthResponseDto getAccessTokenFromCredentials(String email, String password,
                                                         HttpServletResponse response) {

        UserDetails userDetails = authenticateUser(email, password);
        String refreshToken = createAndSaveRefreshToken(userDetails);
        addRefreshTokenCookieToResponse(refreshToken, response);
        return makeAccessTokenResponse(userDetails);
    }

    public AuthResponseDto registerUserAndGetAccessToken(String email, String password,
                                                         HttpServletResponse response) {

        UserDetails userDetails = registerUser(email, password);
        String refreshToken = createAndSaveRefreshToken(userDetails);
        addRefreshTokenCookieToResponse(refreshToken, response);
        return makeAccessTokenResponse(userDetails);
    }

    public void registerUserWithoutLogin(String email, String password) {
        registerUser(email, password);
    }

    public AuthResponseDto getAccessTokenFromRefreshToken(String authHeader) {

        if (!authHeader.startsWith("Bearer")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please check token type");
        }

        String refreshToken = authHeader.substring(7); // remove "Bearer " from the header

        var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                .filter(token -> !token.isRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is revoked"));

        User user = refreshTokenEntity.getUser();

        return makeAccessTokenResponse(new SecurityUser(user));
    }
}
