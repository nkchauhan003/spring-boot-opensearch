package com.cb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

/**
 * Main application class for the Spring Boot application.
 * Excludes the default OpenSearch data auto-configuration.
 */
@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class})
public class Application {
    /**
     * Main method to run the Spring Boot application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}