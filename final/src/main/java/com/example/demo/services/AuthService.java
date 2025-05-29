package com.example.demo.services;

import com.example.demo.dtos.JwtResponse;
import com.example.demo.dtos.LoginRequest;

public interface AuthService {
    JwtResponse authenticateAndGenerateToken(LoginRequest request);

}
