package com.degreemap.DegreeMap.users;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void givenNewUser_whenSave_thenSuccess() {
        User newUser = new User("example@example.com", "password123");
        User insertedUser = userRepository.save(newUser);
        assertEquals(entityManager.find(User.class, insertedUser.getId()), newUser);
    }  
}


