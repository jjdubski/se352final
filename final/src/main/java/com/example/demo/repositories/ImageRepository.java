package com.example.demo.repositories;

import com.example.demo.entities.Image;
import com.example.demo.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProperty(Property property);
}
