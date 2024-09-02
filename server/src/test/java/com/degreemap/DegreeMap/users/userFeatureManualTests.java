package com.degreemap.DegreeMap.users;

import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.degreemap.DegreeMap.auth.AuthController;
import com.degreemap.DegreeMap.auth.AuthService;
import com.degreemap.DegreeMap.config.PasswordEncoderConfig;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalog;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogController;
import com.degreemap.DegreeMap.courseEntities.catalogs.CourseCatalogRepository;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMap;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapController;
import com.degreemap.DegreeMap.dmEntities.degreeMap.DegreeMapRepository;
import com.degreemap.DegreeMap.users.userCatalog.UserCourseCatalog;
import com.degreemap.DegreeMap.users.userDm.UserDegreeMap;
import com.degreemap.DegreeMap.users.userDm.UserDegreeMapController;
import com.degreemap.DegreeMap.users.userDm.UserDegreeMapRepository;

@WebMvcTest(controllers = {UserController.class, DegreeMapController.class, CourseCatalogController.class, AuthController.class, UserDegreeMapController.class, })
@Import(PasswordEncoderConfig.class)
public class userFeatureManualTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;
    @MockBean
    private AuthService authService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private DegreeMapRepository degreeMapRepository;
    @MockBean
    private CourseCatalogRepository courseCatalogRepository;
    @MockBean
    private UserDegreeMapRepository udmRepo;

    @Test
    public void manuallyTestUsers() throws Exception {
        User user = new User("degreeMap@gmail.com", passwordEncoder.encode("degreeMap"));
        user.setId(1L);
        given(userService.getUserById(1L)).willReturn(user);

        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        // given(userService.getUserById(1L)).willReturn(user);


        // mockMvc.perform(post("/api/auth/register")
        //         .contentType(MediaType.APPLICATION_JSON)
        //         .content("{\"email\":\"" + user.getEmail() + "\", \"password\":\"" + user.getPassword() + "\"}"))
        //         .andExpect(status().isOk())
        //         .andExpect(jsonPath("$.name").value(user.getEmail()))
        //         .andReturn();

        MvcResult userResult = mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andReturn();

        System.out.println("\n!!!! User Data !!!! \n" + userResult.getResponse().getContentAsString());

        
        DegreeMap degreeMap = new DegreeMap("SE Associates Degree");
        degreeMap.setId(1L);
        given(degreeMapRepository.save(any(DegreeMap.class))).willReturn(degreeMap);
        given(degreeMapRepository.findById(1L)).willReturn(Optional.of(degreeMap));

        MvcResult dmResult = mockMvc.perform(post("/api/degreeMaps")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"SE Associates Degree\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SE Associates Degree"))
                .andReturn();

        System.out.println("\n!!!! DegreeMap Data !!!! \n" + dmResult.getResponse().getContentAsString());

        UserDegreeMap userDM = new UserDegreeMap(user, degreeMap);
        userDM.setId(1L);

        DegreeMap degreeMap2 = new DegreeMap("CE Minor Degree");
        degreeMap.setId(2L);
        given(degreeMapRepository.save(any(DegreeMap.class))).willReturn(degreeMap2);
        given(degreeMapRepository.findById(2L)).willReturn(Optional.of(degreeMap2));

        MvcResult dm2Result = mockMvc.perform(post("/api/degreeMaps")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"CE Minor Degree\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("CE Minor Degree"))
                .andReturn();
                
        System.out.println("\n!!!! DegreeMap 2 Data !!!! \n" + dm2Result.getResponse().getContentAsString());
                
        UserDegreeMap userDM2 = new UserDegreeMap(user, degreeMap2);
        userDM2.setId(2L);

        MvcResult newUserResult = mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andReturn();

        System.out.println("\n!!!! User Data !!!! \n" + newUserResult.getResponse().getContentAsString());

        CourseCatalog rit = new CourseCatalog("RIT Course Catalog");
        rit.setId(1L);
        given(courseCatalogRepository.findById(1L)).willReturn(Optional.of(rit));
        given(courseCatalogRepository.save(any(CourseCatalog.class))).willReturn(rit);

        MvcResult ritCatalogResult = mockMvc.perform(post("/api/course-catalogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + rit.getName() + "\"}"))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").value("RIT Course Catalog"))
                        .andReturn();

        System.out.println("\n!!!! RIT Catalog 1 Data !!!! \n" + ritCatalogResult.getResponse().getContentAsString());

        UserCourseCatalog ucc1 = new UserCourseCatalog(user, rit);
        ucc1.setId(1L);

        CourseCatalog mcc = new CourseCatalog("MCC Course Catalog");
        mcc.setId(2L);
        given(courseCatalogRepository.findById(2L)).willReturn(Optional.of(mcc));
        given(courseCatalogRepository.save(any(CourseCatalog.class))).willReturn(mcc);

        MvcResult mccCatalogResult = mockMvc.perform(post("/api/course-catalogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + mcc.getName() + "\"}"))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").value("MCC Course Catalog"))
                        .andReturn();

        System.out.println("\n!!!! MCC Catalog 2 Data !!!! \n" + mccCatalogResult.getResponse().getContentAsString());

        UserCourseCatalog ucc2 = new UserCourseCatalog(user, mcc);
        ucc2.setId(2L);

        MvcResult userResult2 = mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andReturn();

        System.out.println("\n!!!! User Data !!!! \n" + userResult2.getResponse().getContentAsString());
        
        /*

{
  "id": 1,
  "email": "degreeMap@gmail.com",
  "password": "$2a$10$hsUD1NUnr1oZb9Y40REZM.E9aNOXhwy/DMm1NxIhwysVfoWe1CVhe",
  "refreshTokens": null,
  "userCCs": [
    {
      "id": 1,
      "courseCatalog": {
        "id": 1,
        "name": "RIT Course Catalog",
        "courses": []
      }
    },
    {
      "id": 2,
      "courseCatalog": {
        "id": 2,
        "name": "MCC Course Catalog",
        "courses": []
      }
    }
  ],
  "userDMs": [
    {
      "id": 1,
      "degreeMap": {
        "id": 2,
        "name": "SE Associates Degree",
        "years": []
      }
    },
    {
      "id": 2,
      "degreeMap": {
        "id": null,
        "name": "CE Minor Degree",
        "years": []
      }
    }
  ]
}

        */

    }
}
