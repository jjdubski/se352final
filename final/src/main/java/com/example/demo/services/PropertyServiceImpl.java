package com.example.demo.services;

import com.example.demo.entities.Image;
import com.example.demo.entities.Property;
import com.example.demo.repositories.PropertyRepository;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    public final PropertyRepository propertyRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public List<Image> getImages(Long id) {
        List<Image> image = propertyRepository.getAllPropertyImages(id);
        return image;
    }

    @Override
    public Property getProperty(Long id) {
        Property property = propertyRepository.getReferenceById(id);
        return property;
    }
}
