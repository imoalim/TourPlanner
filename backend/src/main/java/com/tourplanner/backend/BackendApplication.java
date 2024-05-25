package com.tourplanner.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(".env"));
			lines.stream()
					.map(line -> line.split("="))
					.forEach(parts -> System.setProperty(parts[0], parts[1]));
		} catch (IOException e) {
			System.out.println("Error parsing env variables: " + e);
		}
		SpringApplication.run(BackendApplication.class, args);
	}
}
