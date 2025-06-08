package com.example.demo.initializers;

import com.example.demo.entities.*;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.PropertyRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
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

        public DataInitializer(PropertyRepository propertyRepository, UserRepository userRepository,
                        PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                        ImageRepository imageRepository) {
                this.propertyRepository = propertyRepository;
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.roleRepository = roleRepository;
                this.imageRepository = imageRepository;
        }

        @PostConstruct
        public void init() {
                if (userRepository.count() != 0 || propertyRepository.count() != 0 || roleRepository.count() != 0) {
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
                /////Properties
                ////////////////////


                Property property1 = new Property(
                                "3818 N Christiana Ave",
                                1025000.00,
                                "Chicago, Il",
                                3600,
                                "Beautifully redesigned single-family home with open-concept living, chef's kitchen, spacious bedrooms, multiple balconies, and a landscaped yard in a sought-after Chicago neighborhood.",
                                user2);

                Property property2 = new Property(
                                "3423 N Kedzie Ave",
                                899000.00,
                                "Chicago, Il",
                                4600,
                                "Oversized all-brick home with 6 bedrooms, high ceilings, large kitchen, in-law suite, roof deck, and professionally landscaped yard near Belmont Blue Line in Chicago.",
                                user6);


                Property property3 = new Property(
                        "1837 N Fremont St",
                        3795000.59,
                        "Chicago, Il",
                        4662,
                        "Welcome to this architectural masterpiece, nestled in the heart of Lincoln Park on the serene, tree-lined Fremont Street.",
                        user7
                        );

                propertyRepository.save(property1);
                propertyRepository.save(property2);
                propertyRepository.save(property3);

                //////////////////////
                /// Images
                /////////////////////

                Image image1 = new Image("property-image1.webp", property1);
                Image image2 = new Image("property-image2.webp", property2);
                Image image3 = new Image("property-image3.webp", property3);

                imageRepository.save(image1);
                imageRepository.save(image2);
                imageRepository.save(image3);

                property1.addToPropertyImages(image1);
                property2.addToPropertyImages(image2);
                property3.addToPropertyImages(image3);

                /////////////
                //Messages
                /////////////

                Message message1 = new Message(user1,
                        user2,
                        "Hi! I am interested in learning more about the property",
                        property1,
                        null);
                user2.getMessagesSent().add(message1);

                Message message3 = new Message(
                        user4,
                        user6,
                        "Is the property at 3423 N Kedzie Ave still available?",
                        property2,
                        "Yes it is!");
                user4.getMessagesSent().add(message3);



        }

}
