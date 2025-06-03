package com.example.demo.entities;


import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)  // EAGER fetch to load roles during login
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column()
    private String profilePicture; // stores filename or relative path

<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
    @Column(nullable = false)
    private Timestamp createdAt;

    @OneToMany(mappedBy = "listingAgent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Property> propertiesListed;



//    @OneToMany
//    private List<Property> propertiesFavorited;
//
//    @OneToMany
//    private List<Message> messagesSent;
//
//    @OneToMany
//    private List<Message> messagesReceived;

    public User() {}

    public User(String password, String firstName, String lastName,
                String email,Set<Role> roles, String profilePicture) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
        this.profilePicture = profilePicture;
    }

<<<<<<< Updated upstream
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
=======
>>>>>>> Stashed changes

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) { this.roles = roles; }

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

    public void setAgent(User user) {

    }



//    public List<Property> getPropertiesFavorited() {
//        return propertiesFavorited;
//    }
//
//    public void setPropertiesFavorited(List<Property> propertiesFavorited) {
//        this.propertiesFavorited = propertiesFavorited;
//    }
//
//    public List<Message> getMessagesSent() {
//        return messagesSent;
//    }
//
//    public void setMessagesSent(List<Message> messagesSent) {
//        this.messagesSent = messagesSent;
//    }
//
//    public List<Message> getMessagesReceived() {
//        return messagesReceived;
//    }
//
//    public void setMessagesReceived(List<Message> messagesReceived) {
//        this.messagesReceived = messagesReceived;
//    }
}
