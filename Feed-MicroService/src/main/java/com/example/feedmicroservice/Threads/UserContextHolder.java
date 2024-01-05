package com.example.feedmicroservice.Threads;

import com.example.feedmicroservice.DTO.UserDTO;

public class UserContextHolder {
    private static final ThreadLocal<UserDTO> userContext = new ThreadLocal<>();

    public static void setUserInfo(UserDTO userInfo) {
        userContext.set(userInfo);
    }

    public static UserDTO getUserInfo() {
        return userContext.get();
    }

    public static void clear() {
        userContext.remove();
    }
}
