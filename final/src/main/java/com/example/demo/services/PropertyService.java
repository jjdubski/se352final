package com.example.demo.services;

import com.example.demo.entities.Property;
import com.example.demo.entities.User;
import org.springframework.ui.Model;

import java.util.List;

public interface PropertyService {

    List<Property> getPropertiesForCurrentAgent(User agent);

    Object getAllProperties();
}
