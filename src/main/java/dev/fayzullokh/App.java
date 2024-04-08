package dev.fayzullokh;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@OpenAPIDefinition
@EnableDiscoveryClient
@EnableJpaRepositories
@EnableMongoRepositories
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}