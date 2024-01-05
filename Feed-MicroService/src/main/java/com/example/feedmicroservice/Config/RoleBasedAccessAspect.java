package com.example.feedmicroservice.Config;
import com.example.feedmicroservice.DTO.UserDTO;
import com.example.feedmicroservice.Exceptions.CustomAccessDeniedException;
import com.example.feedmicroservice.Threads.UserContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;

@Aspect
@Component
public class RoleBasedAccessAspect {


    @Before("@annotation(requiredRole)")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void checkRequiredRole(JoinPoint joinPoint, RequiredRole requiredRole) throws CustomAccessDeniedException {
        UserDTO userInfo = UserContextHolder.getUserInfo();

        if (userInfo == null || !hasRequiredRole(userInfo.getRole(), requiredRole.value())) {
            String errorMessage = "Access denied. User does not have the required role(s): " + String.join(", ", requiredRole.value());
            String customMessage = "You do not have the required permissions to access this resource.";
            throw new CustomAccessDeniedException(errorMessage, customMessage);
        }
    }

    private boolean hasRequiredRole(String userRole, String[] requiredRoles) {
        // Check if the user role is included in the set of required roles
        return Arrays.asList(requiredRoles).contains(userRole);
    }
}
