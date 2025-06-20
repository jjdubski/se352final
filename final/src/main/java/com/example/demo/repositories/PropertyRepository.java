package com.example.demo.repositories;

import com.example.demo.entities.Image;
import com.example.demo.entities.Property;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    @Query("SELECT p FROM Property p WHERE p.listingAgent.id = :userId")
    List<Property> findAllByUserId(Long userId);

    @Query("SELECT p.propertyImages FROM Property p WHERE p.id = :propertyId")
    List<Image> getAllPropertyImages(@Param("propertyId") Long propertyId);

    List<Property> findByListingAgent(User agent);
}
