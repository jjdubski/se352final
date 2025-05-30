package com.example.demo.entities;


import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Role role;

    @Column()
    private String profilePicture;

    @Column(nullable = false)
    private Timestamp createdAt;

    @OneToMany(mappedBy = "listingAgent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Property> propertiesListed;

    @OneToMany
    private List<Property> propertiesFavorited;

    @OneToMany
    private List<Message> messages;


    public User() {}

    public User(String firstName, String lastName, String email,
                String password, Role role, String profilePicture,
                Timestamp createdAt, List<Property> propertiesListed) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.profilePicture = profilePicture;
        this.createdAt = createdAt;
        this.propertiesListed = propertiesListed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Enum getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt() {
        Date date = new Date();
        this.createdAt = new Timestamp(date.getTime());
    }

    public List<Property> getPropertiesListed() {
        return propertiesListed;
    }

    public void setPropertiesListed(List<Property> propertiesListed) {
        this.propertiesListed = propertiesListed;
    }

    public List<Property> getPropertiesFavorited() {
        return propertiesFavorited;
    }

    public void setPropertiesFavorited(List<Property> propertiesFavorited) {
        this.propertiesFavorited = propertiesFavorited;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addToPropertiesListed(Property property){
        this.propertiesListed.add(property);
        property.setListingAgent(this);
    }

    public void removePropertyListed(Property property){
        this.propertiesListed.remove(property);
    }

    public void addToPropertiesFavorited(Property property){
        this.propertiesFavorited.add(property);
        property.addToUsersFavorited(this);
    }

    public void addToMessages(Message message){
        this.messages.add(message);
    }

    public void removeMessage(Message message){
        this.messages.remove(message);
    }


}
