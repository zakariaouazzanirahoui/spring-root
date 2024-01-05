package com.example.feedmicroservice.Feign;


import com.example.feedmicroservice.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "users-microservice", url = "http://localhost:8089/users-microservice/")
public interface UserClient {

    @GetMapping("/{userId}")
    UserDTO getUserInfo(@PathVariable("userId") Long userId);

    @PostMapping("/bulk")
    Map<Long, UserDTO> getUsersInfo(@RequestParam Set<Long> userIds);

    @GetMapping("/validate-token")
    UserDTO validateToken(@RequestHeader("Authorization") String authorizationHeader);
}
