package com.degreemap.DegreeMap;

import com.degreemap.DegreeMap.auth.RSAKeyRecord;
import com.degreemap.DegreeMap.users.User;
import com.degreemap.DegreeMap.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyRecord.class)
public class DegreeMapApplication {
	public static void main(String[] args) {
		SpringApplication.run(DegreeMapApplication.class, args);
	}

	@Bean
	@Profile("dev")
	CommandLineRunner commandLineRunner(UserRepository userRepository,
										PasswordEncoder encoder) {
		return args -> {
			User u1 = new User("u1@god.com", encoder.encode("thepass"));
			User u2 = new User("u2@god.com", encoder.encode("thepass"));

			userRepository.saveAll(List.of(u1, u2));
		};
	}
}
