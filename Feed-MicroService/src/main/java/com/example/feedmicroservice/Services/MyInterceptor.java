package com.example.feedmicroservice.Services;


import com.example.feedmicroservice.DTO.UserDTO;
import com.example.feedmicroservice.Feign.UserClient;
import com.example.feedmicroservice.Threads.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MyInterceptor implements HandlerInterceptor {
    @Lazy
    @Autowired
    private UserClient userClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            UserDTO result = userClient.validateToken(authorizationHeader);
            UserContextHolder.setUserInfo(result);

        }


        return true;

    }
}