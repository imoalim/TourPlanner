package com.tourplanner.backend.service.util;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EnvLoader {
    public static void loadEnvVariables() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(".env"));
            lines.stream()
                    .map(line -> line.split("="))
                    .forEach(parts -> System.setProperty(parts[0], parts[1]));
        } catch (IOException e) {
            System.out.println("Error parsing env variables: " + e);
        }
    }
}
