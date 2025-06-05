package com.example.demo.services;

import com.example.demo.entities.Property;
import com.example.demo.entities.User;
import com.example.demo.repositories.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    PropertyRepository propertyRepository;



    @Override
    public List<Property> getPropertiesForCurrentAgent(User agent) {
        return propertyRepository.findAllByUser(agent);
    }


}
