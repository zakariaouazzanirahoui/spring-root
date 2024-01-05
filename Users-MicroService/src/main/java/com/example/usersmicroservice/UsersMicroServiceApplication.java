package com.example.usersmicroservice;

import com.example.usersmicroservice.Config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@EnableDiscoveryClient
@SpringBootApplication
@RestController
@EnableConfigurationProperties(RsaKeyProperties.class)
public class UsersMicroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersMicroServiceApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("refreshhhh %s!", name);
    }
    @GetMapping("/admin")
    public String admin() {
        return "ADMINITION!";
    }
    @GetMapping("/editor")
    public String editor() {
        return "EDITIONS!";
    }
}
