package com.example.demo.services;

import com.example.demo.entities.Image;
import com.example.demo.entities.Property;
import com.example.demo.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService {

    List<Property> getPropertiesForCurrentAgent(User agent);

    List<Property> getAllProperties();

    Property findPropertyById(Long id);

    List<Image> getImagesForProperty(Long id);

    @Transactional
    Property addNewProperty(Property property, List<MultipartFile> files);

    @Transactional
    Property editProperty(Property property, Property updatedProperty);

    @Transactional
    Property deleteProperty(Long id);

//     List<Image> getImages(Long id);

//     Property getProperty(Long id);
}
