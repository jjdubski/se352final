package com.example.demo.services;

import com.example.demo.entities.Image;
import com.example.demo.entities.Property;
import com.example.demo.entities.User;
import com.example.demo.exceptions.PropertyNotFoundException;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {

    PropertyRepository propertyRepository;
    ImageRepository imageRepository;

    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository, ImageRepository imageRepository) {
        this.propertyRepository = propertyRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Property> getPropertiesForCurrentAgent(User agent) {
        // need to fix this
        return null;
//        return propertyRepository.findAllByUserId(agent.getId());
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

    @Override
    public List<Image> getImagesForProperty(Long id) {
        Optional<Property> property = propertyRepository.findById(id);
        if (property.isEmpty()){   //no property w/ given id
            throw new PropertyNotFoundException("No property with given id");
        }
        return imageRepository.findByProperty(property.get());
    }

//     public final PropertyRepository propertyRepository;

//     public PropertyServiceImpl(PropertyRepository propertyRepository) {
//         this.propertyRepository = propertyRepository;
//     }

//     @Override
//     public List<Image> getImages(Long id) {
//         List<Image> image = propertyRepository.getAllPropertyImages(id);
//         return image;
//     }

//     @Override
//     public Property getProperty(Long id) {
//         Property property = propertyRepository.getReferenceById(id);
//         return property;
//     }
}
