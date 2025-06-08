package com.example.demo.initializers;

import com.example.demo.entities.*;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.MessageRepository;
import com.example.demo.repositories.PropertyRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.annotation.PostConstruct;

import org.hibernate.mapping.Array;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer {
        private final PropertyRepository propertyRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final RoleRepository roleRepository;
        private final ImageRepository imageRepository;
        private final MessageRepository messageRepository;

        public DataInitializer(PropertyRepository propertyRepository, UserRepository userRepository,
                        PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                        ImageRepository imageRepository, MessageRepository messageRepository) {
                this.propertyRepository = propertyRepository;
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.roleRepository = roleRepository;
                this.imageRepository = imageRepository;
                this.messageRepository = messageRepository;
        }

        @PostConstruct
        public void init() {
                if (userRepository.count() != 0) {
                        System.out.println("Data already present - not executing initalizer");
                        return;
                }

                Role roleBuyer = new Role("BUYER");
                Role roleAdmin = new Role("ADMIN");
                Role roleAgent = new Role("AGENT");

                roleRepository.save(roleBuyer);
                roleRepository.save(roleAdmin);
                roleRepository.save(roleAgent);

                /////////////////////
                ///// Users
                /////////////////////

                Set<Role> user1Roles = new HashSet<>();
                user1Roles.add(roleBuyer);

                Set<Role> user2Roles = new HashSet<>();
                user2Roles.add(roleAgent);

                Set<Role> user3Roles = new HashSet<>();
                user3Roles.add(roleAdmin);

                Set<Role> user4Roles = new HashSet<>();
                user4Roles.add(roleBuyer);

                Set<Role> user5Roles = new HashSet<>();
                user5Roles.add(roleBuyer);

                Set<Role> user6Roles = new HashSet<>();
                user6Roles.add(roleAgent);

                Set<Role> user7Roles = new HashSet<>();
                user7Roles.add(roleAgent);

                User user1 = new User(passwordEncoder.encode("bj.123"),
                                "Bob",
                                "Johnson",
                                "johnson@email.com",
                                user2Roles,
                                "image1.jpg");
                user1.setCreatedAt();

                User user2 = new User(passwordEncoder.encode("mj.123"),
                                "Maria",
                                "Jackson",
                                "jackson@email.com",
                                user1Roles,
                                "image2.jpg");
                user2.setCreatedAt();

                User user3 = new User(passwordEncoder.encode("jd.123"),
                                "Jack",
                                "Douglas",
                                "douglas@email.com",
                                user3Roles,
                                "image3.jpg");
                user3.setCreatedAt();

                User user4 = new User(passwordEncoder.encode("rb.123"),
                                "Rachel",
                                "Brown",
                                "rachel@email.com",
                                user4Roles,
                                "image4.jpg");
                user4.setCreatedAt();

                User user5 = new User(passwordEncoder.encode("ts.123"),
                                "Tom",
                                "Smith",
                                "tomsmith@email.com",
                                user5Roles,
                                "image5.jpg");
                user5.setCreatedAt();

                User user6 = new User(passwordEncoder.encode("kw.123"),
                                "Karen",
                                "White",
                                "karen@email.com",
                                user6Roles,
                                "image6.jpg");
                user6.setCreatedAt();

                User user7 = new User(passwordEncoder.encode("am.123"),
                                "Alex",
                                "Martinez",
                                "alex@email.com",
                                user7Roles,
                                "image7.jpg");
                user7.setCreatedAt();

                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);
                userRepository.save(user4);
                userRepository.save(user5);
                userRepository.save(user6);
                userRepository.save(user7);

                /////////////////////
                ///// Properties
                ////////////////////

                ArrayList<Image> emptyImageList = new ArrayList<Image>();
                ArrayList<User> emptyUserList = new ArrayList<User>();

                Property property1 = new Property(
                                "3818 N Christiana Ave",
                                1025000.00,
                                "Chicago, IL",
                                3600,
                                "Beautifully redesigned single-family home with open-concept living, chef's kitchen, spacious bedrooms, multiple balconies, and a landscaped yard in a sought-after Chicago neighborhood.",
                                user2,
                                emptyImageList,
                                emptyUserList);

                Property property2 = new Property(
                                "3423 N Kedzie Ave",
                                899000.00,
                                "Chicago, IL",
                                4600,
                                "Oversized all-brick home with 6 bedrooms, high ceilings, large kitchen, in-law suite, roof deck, and professionally landscaped yard near Belmont Blue Line in Chicago.",
                                user6,
                                emptyImageList,
                                emptyUserList);

                Property property3 = new Property(
                                "1837 N Fremont St",
                                3795000.59,
                                "Chicago, Il",
                                4662,
                                "Welcome to this architectural masterpiece, nestled in the heart of Lincoln Park on the serene, tree-lined Fremont Street.",
                                user7,
                                emptyImageList,
                                emptyUserList);

                Property property4 = new Property(
                                "1234 W Addison St",
                                750000.00,
                                "Chicago, IL",
                                2800,
                                "Charming 3-bedroom home with hardwood floors, updated kitchen, and a finished basement in Lakeview.",
                                user1,
                                emptyImageList,
                                emptyUserList);

                Property property5 = new Property(
                                "5678 S Michigan Ave",
                                650000.00,
                                "Chicago, IL",
                                3200,
                                "Spacious 4-bedroom townhouse with modern finishes, rooftop deck, and attached garage.",
                                user2,
                                emptyImageList,
                                emptyUserList);

                Property property6 = new Property(
                                "9101 N Clark St",
                                1200000.00,
                                "Chicago, IL",
                                4100,
                                "Luxury 5-bedroom home with chef's kitchen, spa bathrooms, and a large backyard.",
                                user3,
                                emptyImageList,
                                emptyUserList);

                Property property7 = new Property(
                                "2222 W Irving Park Rd",
                                540000.00,
                                "Chicago, IL",
                                2100,
                                "Cozy 2-bedroom condo with balcony, in-unit laundry, and heated parking.",
                                user1,
                                emptyImageList,
                                emptyUserList);

                Property property8 = new Property(
                                "3333 E 79th St",
                                480000.00,
                                "Chicago, IL",
                                1900,
                                "Renovated bungalow with open floor plan, new appliances, and finished basement.",
                                user2,
                                emptyImageList,
                                emptyUserList);

                Property property9 = new Property(
                                "4444 W Belmont Ave",
                                830000.00,
                                "Chicago, IL",
                                3500,
                                "Modern 4-bedroom home with smart features, fenced yard, and two-car garage.",
                                user3,
                                emptyImageList,
                                emptyUserList);

                Property property10 = new Property(
                                "5555 S State St",
                                720000.00,
                                "Chicago, IL",
                                3000,
                                "Classic brick home with updated kitchen, hardwood floors, and large patio.",
                                user1,
                                emptyImageList,
                                emptyUserList);

                Property property11 = new Property(
                                "6666 N Sheridan Rd",
                                950000.00,
                                "Chicago, IL",
                                3700,
                                "Elegant 4-bedroom residence with lake views, gourmet kitchen, and finished attic.",
                                user2,
                                emptyImageList,
                                emptyUserList);

                propertyRepository.save(property1);
                propertyRepository.save(property2);
                propertyRepository.save(property3);
                propertyRepository.save(property4);
                propertyRepository.save(property5);
                propertyRepository.save(property6);
                propertyRepository.save(property7);
                propertyRepository.save(property8);
                propertyRepository.save(property9);
                propertyRepository.save(property10);

                //////////////////////
                /// Images
                /////////////////////

                Image image1 = new Image("property-image1.webp", property1);
                Image image2 = new Image("property-image2.webp", property2);
                Image image3 = new Image("property-image3.webp", property3);
                Image image4 = new Image("house.png", property4);
                Image image5 = new Image("house.png", property5);
                Image image6 = new Image("house.png", property6);
                Image image7 = new Image("house.png", property7);
                Image image8 = new Image("house.png", property8);
                Image image9 = new Image("house.png", property9);
                Image image10 = new Image("house.png", property10);

                imageRepository.save(image1);
                imageRepository.save(image2);
                imageRepository.save(image3);
                imageRepository.save(image4);
                imageRepository.save(image5);
                imageRepository.save(image6);
                imageRepository.save(image7);
                imageRepository.save(image8);
                imageRepository.save(image9);
                imageRepository.save(image10);

                // property1.addToPropertyImages(image1);
                // property2.addToPropertyImages(image2);
                // property3.addToPropertyImages(image3);
                // property4.addToPropertyImages(image4);
                // property5.addToPropertyImages(image5);
                // property6.addToPropertyImages(image6);
                // property7.addToPropertyImages(image7);
                // property8.addToPropertyImages(image8);
                // property9.addToPropertyImages(image9);
                // property10.addToPropertyImages(image10);

                /////////////
                // Messages
                /////////////

                Message message1 = new Message(
                                user1,
                                user2,
                                "Hi! I am interested in learning more about the property.",
                                property1,
                                null);

                Message message2 = new Message(
                                user2,
                                user1,
                                "Thank you for your interest! Would you like to schedule a tour?",
                                property1,
                                null);

                Message message3 = new Message(
                                user4,
                                user6,
                                "Is the property at 3423 N Kedzie Ave still available?",
                                property2,
                                "Yes it is!");

                Message message4 = new Message(
                                user5,
                                user7,
                                "Can you provide more photos of the property at 1837 N Fremont St?",
                                property3,
                                null);

                Message message5 = new Message(
                                user3,
                                user1,
                                "Is the price negotiable for 1234 W Addison St?",
                                property4,
                                null);

                Message message6 = new Message(
                                user6,
                                user2,
                                "How old is the roof on 5678 S Michigan Ave?",
                                property5,
                                null);

                Message message7 = new Message(
                                user7,
                                user3,
                                "Are pets allowed at 9101 N Clark St?",
                                property6,
                                "No sorry. Pets are not allowed.");

                Message message8 = new Message(
                                user1,
                                user2,
                                "Is there parking available at 2222 W Irving Park Rd?",
                                property7,
                                "No, there is no parking available.");

                Message message9 = new Message(
                                user2,
                                user6,
                                "What are the HOA fees for 3333 E 79th St?",
                                property8,
                                null);

                Message message10 = new Message(
                                user3,
                                user1,
                                "Can I schedule a viewing for 4444 W Belmont Ave?",
                                property9,
                                "No, the property is not available.");

                messageRepository.save(message1);
                messageRepository.save(message2);
                messageRepository.save(message3);
                messageRepository.save(message4);
                messageRepository.save(message5);
                messageRepository.save(message6);
                messageRepository.save(message7);
                messageRepository.save(message8);
                messageRepository.save(message9);
                messageRepository.save(message10);
        }

}
