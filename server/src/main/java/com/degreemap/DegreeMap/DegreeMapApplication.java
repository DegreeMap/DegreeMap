package com.degreemap.DegreeMap;

import com.degreemap.DegreeMap.config.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RSAKeyRecord.class)
public class DegreeMapApplication {
	public static void main(String[] args) {
		SpringApplication.run(DegreeMapApplication.class, args);
	}
}
