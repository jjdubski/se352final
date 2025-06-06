package com.example.demo.services;

import com.example.demo.entities.Image;
import com.example.demo.entities.Property;
import com.example.demo.entities.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService {

    List<Property> getPropertiesForCurrentAgent(User agent);

    Object getAllProperties();

    Property findPropertyById(Long id);

    List<Image> getImagesForProperty(Long id);

    Property addNewProperty(Property property, List<MultipartFile> files);

//     List<Image> getImages(Long id);

//     Property getProperty(Long id);
}
