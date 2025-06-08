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
                                "Chicago, Il",
                                3600,
                                "Beautifully redesigned single-family home with open-concept living, chef's kitchen, spacious bedrooms, multiple balconies, and a landscaped yard in a sought-after Chicago neighborhood.",
                                user2,
                                List.of(),
                                List.of());

                Property property2 = new Property(
                                "3423 N Kedzie Ave",
                                899000.00,
                                "Chicago, Il",
                                4600,
                                "Oversized all-brick home with 6 bedrooms, high ceilings, large kitchen, in-law suite, roof deck, and professionally landscaped yard near Belmont Blue Line in Chicago.",
                                user3,
                                List.of(),
                                List.of());

                propertyRepository.save(property1);
                propertyRepository.save(property2);

                //////////////////////
                /// Images
                /////////////////////

                Image image1 = new Image("1743280986563_8c1815366539cf90fd6cb38dbb1b1e1e-cc_ft_960.jpg", property1);
                Image image2 = new Image("1743280986566_3bd01c92edfab81e6ef7702df5c5f315-cc_ft_960.jpg", property2);
                Image image3 = new Image("1743280986566_419c22f5dd1ddc1a6d861df85c941db9-cc_ft_960.jpg", property1);

                imageRepository.save(image1);
                imageRepository.save(image2);
                imageRepository.save(image3);
        }

}
