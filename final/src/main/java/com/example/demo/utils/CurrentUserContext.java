package com.example.demo.utils;

import com.example.demo.entities.User;
import org.springframework.security.core.Authentication;

public record CurrentUserContext(User user, Authentication auth) {}
