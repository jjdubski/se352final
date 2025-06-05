package com.example.demo.services;

import com.example.demo.entities.Property;
import com.example.demo.entities.User;
import com.example.demo.exceptions.PropertyNotFoundException;
import com.example.demo.repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {

    PropertyRepository propertyRepository;

    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public List<Property> getPropertiesForCurrentAgent(User agent) {
        return propertyRepository.findAllByUser(agent);
    }

    @Override
    public Object getAllProperties() {
        return propertyRepository.findAll();
    }

    @Override
    public Property findPropertyById(Long id) {
        Optional<Property> property = propertyRepository.findById(id);
        if (property.isEmpty()){   //no property w/ given id
            throw new PropertyNotFoundException("No property with given id");
        }
        return property.get();
    }


}
