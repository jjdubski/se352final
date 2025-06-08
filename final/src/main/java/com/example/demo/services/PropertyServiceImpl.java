package com.example.demo.services;

import com.example.demo.entities.Image;
import com.example.demo.entities.Property;
import com.example.demo.entities.User;
import com.example.demo.exceptions.InvalidPropertyImageException;
import com.example.demo.exceptions.InvalidPropertyParameterException;
import com.example.demo.exceptions.PropertyNotFoundException;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.PropertyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
//        return propertyRepository.findByListingAgent(agent);
        return propertyRepository.findAllByUserId(agent.getId());
    }

    @Override
    public List<Property> getAllProperties() {
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

    @Transactional
    @Override
    public Property addNewProperty(Property property, List<MultipartFile> files) {
        validateProperty(property);

        // adding images
        if (files != null){
            for (MultipartFile file : files){
                validateFile(file);
                Image image = new Image(file.getName(), property);
                property.addToPropertyImages(image);
            }
        }
        return propertyRepository.save(property);
    }

    @Transactional
    @Override
    public Property editProperty(Property property, Property updatedProperty) {
        validateProperty(updatedProperty);

        property.setTitle(updatedProperty.getTitle());
        property.setPrice(updatedProperty.getPrice());
        property.setLocation(updatedProperty.getLocation());
        property.setSize(updatedProperty.getSize());
        property.setDescription(updatedProperty.getDescription());

        return property;
    }


    // Validation Methods
    private void validateProperty(Property property) {
        validateTitle(property);
        validatePrice(property);
        validateLocation(property);
        validateSize(property);
        validateListingAgent(property);
    }

    private void validateSize(Property property) {
        if (property.getSize() == null){
            throw new InvalidPropertyParameterException("Size cannot be null.");
        }
    }

    private void validateListingAgent(Property property) {
        if (property.getListingAgent() == null){
            throw new InvalidPropertyParameterException("Listing agent cannot be null.");
        }
    }

    private void validateLocation(Property property) {
        if (property.getLocation() == null){
            throw new InvalidPropertyParameterException("Location cannot be null.");
        }
    }

    private void validatePrice(Property property) {
        if (property.getPrice() == null){
            throw new InvalidPropertyParameterException("Price cannot be null.");
        }
    }

    private void validateTitle(Property property) {
        if (property.getTitle() == null){
            throw new InvalidPropertyParameterException("Title cannot be null.");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null){
            throw new InvalidPropertyImageException("Uploaded null image.");
        }
        validateFileName(file.getOriginalFilename());
    }

    private void validateFileName(String originalFilename) {
        Integer len = originalFilename.length();
        if (!(len >= 4 && originalFilename.regionMatches(true, len - 4, ".jpg", 0, 4))
        || !(len >= 4 && originalFilename.regionMatches(true, len - 4, ".png", 0, 4))){
            throw new InvalidPropertyImageException("File must be .png or .jpg.");
        }
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
