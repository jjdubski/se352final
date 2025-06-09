package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer size;

    @Column(length = 5000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "listing_agent")
    @JsonIgnore
    private User listingAgent;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Image> propertyImages;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> usersFavorited;

    public Property() {
    }

    public Property(String title, Double price, String location,
            Integer size, String description, User listingAgent,
            List<Image> propertyImages, List<User> usersFavorited) {
        this.title = title;
        this.price = price;
        this.location = location;
        this.size = size;
        this.description = description;
        this.listingAgent = listingAgent;
        this.propertyImages = propertyImages;
        this.usersFavorited = usersFavorited;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getListingAgent() {
        return listingAgent;
    }

    public void setListingAgent(User listingAgent) {
        this.listingAgent = listingAgent;
    }

    public List<Image> getPropertyImages() {
        return propertyImages;
    }

    public List<User> getUsersFavorited() {
        return usersFavorited;
    }

    // has to be done through service/repository
    public void addToPropertyImages(Image image) {
        this.propertyImages.add(image);
        image.setProperty(this);
    }

    public void removePropertyImage(Image image) {
        this.propertyImages.remove(image);
    }

    public void addToUsersFavorited(User user) {
        this.usersFavorited.add(user);
    }

    public void removeFromUsersFavorited(User user) {
        this.usersFavorited.remove(user);
    }
}
