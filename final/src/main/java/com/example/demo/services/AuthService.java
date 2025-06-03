package com.example.demo.services;

import com.example.demo.dtos.JwtResponse;
import com.example.demo.dtos.LoginRequest;
import com.example.demo.entities.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    JwtResponse authenticateAndGenerateToken(User user);

    Cookie loginAndCreateJwtCookie(User user);

    void clearJwtCookie(HttpServletResponse response);
}
