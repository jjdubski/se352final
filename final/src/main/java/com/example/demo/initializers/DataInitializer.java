package com.example.demo.initializers;

import com.example.demo.entities.Image;
import com.example.demo.entities.Property;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.PropertyRepository;
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

    public DataInitializer(PropertyRepository propertyRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }


    @PostConstruct
    public void init(){
        if (userRepository.count() != 0 || propertyRepository.count() != 0){
            System.out.println("Data already present - not executing initalizer");
            return;
        }




        /////////////////////
        /////Users
        /////////////////////


        Set<Role> user1Roles = new HashSet<>();
        Role user1Role = new Role("AGENT");
        user1Roles.add(user1Role);

        Set<Role> user2Roles = new HashSet<>();
        Role user2Role = new Role("MANAGER");
        user2Roles.add(user2Role);

        Set<Role> user3Roles = new HashSet<>();
        Role user3Role = new Role("ADMIN");
        user3Roles.add(user3Role);

        User user1 = new User(passwordEncoder.encode("bj.123"),
                "Bob",
                "Johnson",
                "johnson@email.com",
                user2Roles,
                "image1.jpg");

        User user2 = new User(passwordEncoder.encode("mj.123"),
                "Maria",
                "Jackson",
                "jackson@email.com",
                user1Roles,
                "image2.jpg");

        User user3 = new User(passwordEncoder.encode("jd.123"),
                "Jack",
                "Douglas",
                "douglas@email.com",
                user3Roles,
                "image3.jpg");

        Property property1 = new Property(
                "3818 N Christiana Ave",
                1025000.00,
                "Chicago, Il",
                3600,
                "Experience luxury living in this beautifully redesigned single-family home, where expert craftsmanship and meticulous attention to detail shine throughout. The sun-drenched open-concept main level boasts a seamless flow between the living and dining areas, complemented by a cozy family room featuring a gas fireplace and elegant doors leading to the rear patio & spacious back yard. The stunning chef's kitchen is a true showpiece, featuring custom cabinetry, a designer tile backsplash, high-end stainless steel appliances, and an expansive center island-perfect for entertaining. Upstairs, the spacious primary suite offers a spa-like retreat with a luxurious ensuite featuring a frameless glass shower and dual vanities. Step through the patio doors to enjoy a large private balcony, perfect for morning coffee and fresh air. Two additional bedrooms, a stylish full bath, and generous storage complete the second floor, including a bright and airy bedroom that features a beautiful terrace overlooking the tree-lined street-an inviting space to relax and enjoy the neighborhood views. The sunlit third level is the epitome of this home, offering a bright and inviting alternative to a traditional basement. This exceptional space features a fourth bedroom, another full guest bath, a dedicated laundry area, and a versatile, spacious, recreation room complete with a wet bar, wine fridge, and a walk-out terrace. Flooded with natural light, it provides the perfect blend of comfort and functionality for both relaxing and entertaining. Outside, the professionally landscaped sizable yard offers a picturesque setting, while the two-car garage adds convenience. Situated in a sought-after neighborhood, this stunning home boasts incredible curb appeal and is truly a must-see! All of the outdoor spaces, including the charming front porch, balconies off of multiple rooms, rear patio, and beautifully designed yard, are unique highlights that enhance this homes appeal!",
                );
        Property property2 = new Property(
                "3423 N Kedzie Ave",
                899000.00,
                "Chicago, Il",
                4600,
                "Oversized all-brick single-family home in East Avondale between Logan Square and Irving Park.  Approximately 4600 sq. ft.  6 large bedrooms and 3.5 baths.  Front entrance foyer and great mudroom in the rear.  High ceilings.  Large eat-in kitchen with tons of cabinets and big walk-in pantry.  The top floor has 3 big bedrooms including an enormous master suite with his and her walk-in closets, a large laundry room, and skylights.  The lower level has good natural light with 9-foot ceilings and a true in-law/2nd unit suite with another 3 huge bedrooms/bath/living room/full kitchen and a 2nd laundry room!  2 zones of heating and cooling.  Interior/exterior drain tiles.  Professionally landscaped and gated front and back yards with wonderful perennial plants.  2.5 car garage with room for storage.  Terrific roof deck with trex decking and pergola!  Great location only 3 blocks to the Belmont Blue Line stop.  Easy access to 90/94.  Near great public and private schools, 312 RiverRun trail system, parks, taprooms, coffee shops, restaurants, climbing gym, Chicago river boathouses, and more!  The house has been very well maintained and is move-in ready!",
                user2,
                );


        //////////////////////
        ///Images
        /////////////////////

        Image image1 = new Image("1743280986563_8c1815366539cf90fd6cb38dbb1b1e1e-cc_ft_960.jpg", property1);
        Image image2 = new Image("1743280986566_3bd01c92edfab81e6ef7702df5c5f315-cc_ft_960.jpg", property2);
        Image image3 = new Image("1743280986566_419c22f5dd1ddc1a6d861df85c941db9-cc_ft_960.jpg", property1);



    }

}
