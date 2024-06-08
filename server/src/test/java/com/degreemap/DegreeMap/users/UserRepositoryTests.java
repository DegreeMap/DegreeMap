package com.degreemap.DegreeMap.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void givenNewUser_whenSave_thenSuccess() {
        User newUser = new User("example@example.com", "Password1234!");
        User insertedUser = userRepository.save(newUser);
        assertEquals(entityManager.find(User.class, insertedUser.getId()), newUser);
    }  

    @Test
    void givenTwoUsers_whenSameEmail_whenSave_thenFailure() {
        User email1 = new User("example@example.com", "Password1111!");
        userRepository.save(email1);
        User email2 = new User("example@example.com", "Password2222!");
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(email2);
        });
    }  

    @Test
    void concurrent_GivenTwoUsers_whenSameEmail_whenSave_thenFailure() {
        assertThrows(CompletionException.class, () -> {
            CompletableFuture<Void> user1 = CompletableFuture.runAsync(() ->
                userRepository.save(new User("concurrent@example.com", "Password1234!"))
            );
            CompletableFuture<Void> user2 = CompletableFuture.runAsync(() ->
                userRepository.save(new User("concurrent@example.com", "Password1234!"))
            );
            CompletableFuture.allOf(user1, user2).join();
        });
    }  

    @Test
    void whenNullEmail_whenSave_thenFailure() {
        User user = new User(null, "Password1234!");
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user);
        });
    }

    @Test
    void whenNullPassword_whenSave_thenFailure() {
        User user = new User("example@example.com", null);
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user);
        });
    }

    // For these two, data input will be handled on the front-end
    @Test
    void whenEmptyEmail_whenSave_thenSuccess() {
        User newUser = new User("", "Password1234!");
        User insertedUser = userRepository.save(newUser);
        assertEquals(entityManager.find(User.class, insertedUser.getId()), newUser);
    }
    @Test
    void whenEmptyPassword_whenSave_thenSuccess() {
        User newUser = new User("example@example.com", "");
        User insertedUser = userRepository.save(newUser);
        assertEquals(entityManager.find(User.class, insertedUser.getId()), newUser);
    }

    @Test
    void whenEmailExceedsMaxLength_thenFailure() {
        String longEmail = "a".repeat(256) + "@example.com";
        User user = new User(longEmail, "Password1234!");
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user);
        });
    }

    @Test
    void givenUser_whenSearchByEmail_Success() {
        User newUser = new User("example@example.com", "Password1234!");
        User insertedUser = userRepository.save(newUser);
        assertEquals(entityManager.find(User.class, insertedUser.getId()), newUser);
        User foundUser = userRepository.findByEmail(insertedUser.getEmail());
        assertEquals(insertedUser, foundUser);
    }

    @Test
    void givenUser_whenSearchByEmail_NotFound_Failure() {
        assertEquals(userRepository.findByEmail("Poop"), null);
    }
    
    @Test
    void givenUser_whenSearchByEmail_Empty_Failure() {
        assertEquals(userRepository.findByEmail(null), null);
    }
}


