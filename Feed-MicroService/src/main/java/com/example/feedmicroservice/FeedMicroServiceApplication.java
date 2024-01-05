package com.example.feedmicroservice;

//import com.example.feedmicroservice.Config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
/*@EnableConfigurationProperties(RsaKeyProperties.class)
 */
@ComponentScan(basePackages = {"com.example.feedmicroservice.Services"})
@ComponentScan(basePackages = {"com.example.feedmicroservice.Repositories"})
@ComponentScan(basePackages = {"com.example.feedmicroservice.Controllers"})
@ComponentScan(basePackages = {"com.example.feedmicroservice.Config"})
@ComponentScan(basePackages = {"com.example.feedmicroservice.Exceptions"})
@ComponentScan(basePackages = {"com.example.feedmicroservice.Minio"})



public class FeedMicroServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(FeedMicroServiceApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}



}
