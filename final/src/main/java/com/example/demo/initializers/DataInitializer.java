package com.example.demo.initializers;

import com.example.demo.entities.Image;
import com.example.demo.entities.Property;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
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
                user2Roles.add(roleAdmin);

                Set<Role> user3Roles = new HashSet<>();
                user3Roles.add(roleAgent);

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

                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);

                Property property1 = new Property(
                                "3818 N Christiana Ave",
                                1025000.00,
                                "Chicago, IL",
                                3600,
                                "Beautifully redesigned single-family home with open-concept living, chef's kitchen, spacious bedrooms, multiple balconies, and a landscaped yard in a sought-after Chicago neighborhood.",
                                user2,
                                List.of(),
                                List.of());

                Property property2 = new Property(
                                "3423 N Kedzie Ave",
                                899000.00,
                                "Chicago, IL",
                                4600,
                                "Oversized all-brick home with 6 bedrooms, high ceilings, large kitchen, in-law suite, roof deck, and professionally landscaped yard near Belmont Blue Line in Chicago.",
                                user3,
                                List.of(),
                                List.of());

                Property property3 = new Property(
                                "1234 W Addison St",
                                750000.00,
                                "Chicago, IL",
                                2800,
                                "Charming 3-bedroom home with hardwood floors, updated kitchen, and a finished basement in Lakeview.",
                                user1,
                                List.of(),
                                List.of());

                Property property4 = new Property(
                                "5678 S Michigan Ave",
                                650000.00,
                                "Chicago, IL",
                                3200,
                                "Spacious 4-bedroom townhouse with modern finishes, rooftop deck, and attached garage.",
                                user2,
                                List.of(),
                                List.of());

                Property property5 = new Property(
                                "9101 N Clark St",
                                1200000.00,
                                "Chicago, IL",
                                4100,
                                "Luxury 5-bedroom home with chef's kitchen, spa bathrooms, and a large backyard.",
                                user3,
                                List.of(),
                                List.of());

                Property property6 = new Property(
                                "2222 W Irving Park Rd",
                                540000.00,
                                "Chicago, IL",
                                2100,
                                "Cozy 2-bedroom condo with balcony, in-unit laundry, and heated parking.",
                                user1,
                                List.of(),
                                List.of());

                Property property7 = new Property(
                                "3333 E 79th St",
                                480000.00,
                                "Chicago, IL",
                                1900,
                                "Renovated bungalow with open floor plan, new appliances, and finished basement.",
                                user2,
                                List.of(),
                                List.of());

                Property property8 = new Property(
                                "4444 W Belmont Ave",
                                830000.00,
                                "Chicago, IL",
                                3500,
                                "Modern 4-bedroom home with smart features, fenced yard, and two-car garage.",
                                user3,
                                List.of(),
                                List.of());

                Property property9 = new Property(
                                "5555 S State St",
                                720000.00,
                                "Chicago, IL",
                                3000,
                                "Classic brick home with updated kitchen, hardwood floors, and large patio.",
                                user1,
                                List.of(),
                                List.of());

                Property property10 = new Property(
                                "6666 N Sheridan Rd",
                                950000.00,
                                "Chicago, IL",
                                3700,
                                "Elegant 4-bedroom residence with lake views, gourmet kitchen, and finished attic.",
                                user2,
                                List.of(),
                                List.of());

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

                // Image image1 = new
                // Image("1743280986563_8c1815366539cf90fd6cb38dbb1b1e1e-cc_ft_960.jpg",
                // property1);
                // Image image2 = new
                // Image("1743280986566_3bd01c92edfab81e6ef7702df5c5f315-cc_ft_960.jpg",
                // property2);
                // Image image3 = new
                // Image("1743280986566_419c22f5dd1ddc1a6d861df85c941db9-cc_ft_960.jpg",
                // property1);

                // imageRepository.save(image1);
                // imageRepository.save(image2);
                // imageRepository.save(image3);

                Image image1 = new Image("house.png", property1);
                Image image2 = new Image("house.png", property2);
                Image image3 = new Image("house.png", property3);
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
        }

}
