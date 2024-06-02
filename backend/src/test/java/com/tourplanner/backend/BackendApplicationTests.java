package com.tourplanner.backend;

import com.tourplanner.backend.service.util.EnvLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

	@BeforeAll
	public static void setUp() {
		EnvLoader.loadEnvVariables();
	}

	@Test
	void contextLoads() {
	}
}
