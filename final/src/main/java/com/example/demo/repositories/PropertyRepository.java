package com.example.demo.repositories;

import com.example.demo.entities.Image;
import com.example.demo.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Image> getAllPropertyImages(Long id);


}
