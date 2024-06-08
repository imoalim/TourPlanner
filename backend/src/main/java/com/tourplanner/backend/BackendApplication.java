package com.tourplanner.backend;

import com.tourplanner.backend.service.util.EnvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		EnvLoader.loadEnvVariables();
		SpringApplication.run(BackendApplication.class, args);
	}
}
