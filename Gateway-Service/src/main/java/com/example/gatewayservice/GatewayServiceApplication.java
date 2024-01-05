package com.example.gatewayservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableDiscoveryClient
@SpringBootApplication
@Configuration

public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("users-microservice", r -> r.path("/users-microservice/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://users-microservice"))
                .route("feed-microservice", r -> r.path("/feed-microservice/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://feed-microservice"))
                .build();
    }

}
