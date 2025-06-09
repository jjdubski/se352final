package com.example.demo.initializers;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;
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
        // private final FavoritesRepository favoritesRepository;

        public DataInitializer(PropertyRepository propertyRepository, UserRepository userRepository,
                        PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                        ImageRepository imageRepository, MessageRepository messageRepository) {
                this.propertyRepository = propertyRepository;
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.roleRepository = roleRepository;
                this.imageRepository = imageRepository;
                this.messageRepository = messageRepository;
                // this.favoritesRepository = favoritesRepository;
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
                                "Chicago, IL 60618",
                                3600,
                                "Experience luxury living in this beautifully redesigned single-family home, where expert craftsmanship and meticulous attention to detail shine throughout. The sun-drenched open-concept main level boasts a seamless flow between the living and dining areas, complemented by a cozy family room featuring a gas fireplace and elegant doors leading to the rear patio & spacious back yard. The stunning chef's kitchen is a true showpiece, featuring custom cabinetry, a designer tile backsplash, high-end stainless steel appliances, and an expansive center island-perfect for entertaining. Upstairs, the spacious primary suite offers a spa-like retreat with a luxurious ensuite featuring a frameless glass shower and dual vanities. Step through the patio doors to enjoy a large private balcony, perfect for morning coffee and fresh air. Two additional bedrooms, a stylish full bath, and generous storage complete the second floor, including a bright and airy bedroom that features a beautiful terrace overlooking the tree-lined street-an inviting space to relax and enjoy the neighborhood views. The sunlit third level is the epitome of this home, offering a bright and inviting alternative to a traditional basement. This exceptional space features a fourth bedroom, another full guest bath, a dedicated laundry area, and a versatile, spacious, recreation room complete with a wet bar, wine fridge, and a walk-out terrace. Flooded with natural light, it provides the perfect blend of comfort and functionality for both relaxing and entertaining. Outside, the professionally landscaped sizable yard offers a picturesque setting, while the two-car garage adds convenience. Situated in a sought-after neighborhood, this stunning home boasts incredible curb appeal and is truly a must-see! All of the outdoor spaces, including the charming front porch, balconies off of multiple rooms, rear patio, and beautifully designed yard, are unique highlights that enhance this home's appeal!",
                                user2,
                                emptyImageList,
                                emptyUserList);

                Property property2 = new Property(
                                "3423 N Kedzie Ave",
                                899000.00,
                                "Chicago, IL 60618",
                                4600,
                                "Oversized all-brick single-family home in East Avondale between Logan Square and Irving Park.  Approximately 4600 sq. ft.  6 large bedrooms and 3.5 baths.  Front entrance foyer and great mudroom in the rear.  High ceilings.  Large eat-in kitchen with tons of cabinets and big walk-in pantry.  The top floor has 3 big bedrooms including an enormous master suite with his and her walk-in closets, a large laundry room, and skylights.  The lower level has good natural light with 9-foot ceilings and a true in-law/2nd unit suite with another 3 huge bedrooms/bath/living room/full kitchen and a 2nd laundry room!  2 zones of heating and cooling.  Interior/exterior drain tiles.  Professionally landscaped and gated front and back yards with wonderful perennial plants.  2.5 car garage with room for storage.  Terrific roof deck with trex decking and pergola!  Great location only 3 blocks to the Belmont Blue Line stop.  Easy access to 90/94.  Near great public and private schools, 312 RiverRun trail system, parks, taprooms, coffee shops, restaurants, climbing gym, Chicago river boathouses, and more!  The house has been very well maintained and is move-in ready!",
                                user6,
                                emptyImageList,
                                emptyUserList);

                Property property3 = new Property(
                                "1837 N Fremont St",
                                3795000.00,
                                "Chicago, IL 60614",
                                4662,
                                "Welcome to this architectural masterpiece, nestled in the heart of Lincoln Park on the serene, tree-lined Fremont Street. This spacious, 5-bedroom contemporary haven has been thoughtfully remodeled by renowned designer Donna Mondi, seamlessly blending elegance, modern design, and meticulous attention to detail to create an unparalleled living experience.  From the moment you step inside, you're welcomed by soaring ceilings and an abundance of natural light pouring through two-story front windows, complete with electric-controlled shades that offer the perfect balance of privacy and illumination. The living room radiates warmth with its inviting fireplace, setting the tone for a space that effortlessly flows into the state-of-the-art kitchen.  In this culinary haven, the stunning custom Arabescato Corchia marble island takes center stage, complemented by a handcrafted vintage brass hood and an Italian-inspired water line for cookware. Premium Miele appliances, including an integrated coffee machine, enhance the space, while hand-polished lacquer cabinets exude sophistication. This kitchen is the perfect fusion of beauty and functionality, ideal for both casual gatherings and elegant entertaining.  The dining area, complete with its own fireplace, offers a serene setting to host friends and family. The NanaWall glass door system opens to a tranquil Japanese garden fountain, merging indoor elegance with outdoor serenity. The main level also features a beautifully redesigned powder bath with custom Venetian plaster and a sleek steel and frosted glass sliding door. On the second level, you'll find three generously sized bedrooms, one currently used as a den, along with a spacious bathroom that includes a dual vanity and a walk-in shower. This level also offers a convenient laundry room.  The third level is dedicated entirely to a tranquil primary suite, offering incredible natural light and treetop views of Fremont Street. The spacious walk-in closet, outfitted with custom shelving and drawers, provides ample storage, while a second washer/dryer adds ultimate convenience. The spa-like ensuite bathroom features floating dual vanities and a steam shower, creating a sanctuary of relaxation. Step outside to your private rooftop terrace complete with a custom stainless steel chef's kitchen perfect for outdoor dining. Adorned with vibrant perennials and offering panoramic views of the city skyline, this space also has the ability to accommodate solar panels. A convenient dumbwaiter connects this rooftop paradise to the main kitchen below, enhancing both luxury and functionality.  The lower level offers an expansive family room, a climate-controlled wine cellar, and a private guest suite, ensuring comfort and elegance for every family member or guest. Throughout the home, wide plank walnut flooring and heated floors add warmth and sophistication. The Savant smart system seamlessly controls lighting, climate across seven zones, and entertainment, elevating your home life experience to the next level. Completing this exceptional property is a large, detached 2-car garage with radiant heated floors and an additional rooftop deck, perfect for enjoying the outdoors or gardening. A majestic maple tree graces the front, enhancing the home's curb appeal. Situated just steps from vibrant dining, shopping, and entertainment, this contemporary home on Fremont Street embodies luxury and comfort living at its best.",
                                user7,
                                emptyImageList,
                                emptyUserList);

                Property property4 = new Property(
                                "2818 W Wellington Ave",
                                899000.00,
                                "Chicago, IL 60618",
                                3000,
                                "Experience Unparalleled Luxury! Welcome to this stunning, one-of-a-kind luxury home, where elegance meets modern comfort. Nestled in a prestigious neighborhood, this 3-bedroom, 3.5-bathroom masterpiece boasts breathtaking architecture, high-end finishes, and exceptional craftsmanship. Key Features: Grand entrance with high ceilings & beautiful chandelier. Expansive open concept living space with glass doors. Gourmet chef's kitchen with state-of-the-art appliances, custom cabinetry and more. Then step into the beautiful backyard, a perfect blend of luxury, relaxation, and entertainment. Whether you're hosting gatherings, enjoying a quiet evening, or creating lasting memories, this outdoor has it all! Prime Location: Located in Avondale Neighborhood, this home offers exclusivity, privacy, and convenience-just minutes away from the express ways I90/94, public transportation, great schools, fine dining, and entertainment. Schedule your private showing today and be impressed.",
                                user1,
                                emptyImageList,
                                emptyUserList);

                Property property5 = new Property(
                                "3454 W Potomac Ave",
                                959000.00,
                                "Chicago, IL 60651",
                                4098,
                                "Modern 6 bed, 4.1 bath new construction home on an oversized 30' corner lot! Best price/sf of a new house in the area!  Extra wide floor plan with great natural light. Contemporary black and white color palette with natural wood accents throughout. Open concept main level perfect for entertaining. Spacious combined living/dining area with built-in banquet. Chef's kitchen with striking floor to ceiling black cabinetry and quartz counters, Cafe appliances, large center island with waterfall counter and bar seating, and walk-in pantry. Adjoining family room with patio doors to back deck. Sun-filled primary suite with huge built-out WIC and private bath featuring a frameless glass shower, freestanding tub, and dual sinks. Three additional bedrooms, one with ensuite bath, the other two with shared jack and Jill bath + laundry complete the upper level. Finished lower level includes a spacious recreation room with full wet bar, two bedrooms, full bath, second laundry, extra storage space, and utilities. Great outdoor space! Fully enclosed yard with back deck and detached two-car garage. Conveniently located near restaurants, entertainment, shopping, public transportation and 200-acre Humboldt Park!",
                                user2,
                                emptyImageList,
                                emptyUserList);

                Property property6 = new Property(
                                "461 W Melrose St",
                                3300000.00,
                                "Chicago, IL 60657",
                                5400,
                                "East Lakeview is the setting for this gorgeous contemporary masterpiece. The home is comprised of four levels of living space. The spaces are all accessible by an elevator that can transport you from the basement to the roof and all levels in between. There is a beautiful and spacious living room that combines and flows with the large dining room. The kitchen is  a chef's dream and perfect for those who like to cook, entertain or simply enjoy such wide open space. The kitchen has an island with a beautiful custom made marble countertop, high end appliances, pantry, professional range hood, and custom cabinets. The kitchen has various ceiling heights including an area where it reaches upwards of twenty feet, giving the area sense of unmatched volume. The first floor is complimented by an open family room. The rest of the home includes six ample bedrooms, three full baths and two half baths. The generous sized private primary suite has a large walk in closet leading to one of several outside decks. It is complimented by a complete bathroom including all modern amenities. The  full finished basement has high ceilings and perfectly equipped for your relaxation. Outside is a beautiful fully fenced back yard just waiting for your outside enjoyment. The sellers have made many improvements which include- newer kitchen, new flooring, new windows, newer mechanicals, new light fixtures, custom closets, new drainage and tile in the backyard, heated gutters and downspouts, and high end appliances. All this plus, RM6 zoning, highly coveted parking spaces, and within a half block to Lake Shore drive and the incomparable Lake Michigan. This is truly a gem. Hurry !!!",
                                user3,
                                emptyImageList,
                                emptyUserList);

                Property property7 = new Property(
                                "1741 N Mozart St",
                                849000.00,
                                "Chicago, IL 60647",
                                2631,
                                "Reimagined Logan Square single-family home that's been carefully curated by the owner/designer. Three bedrooms up, with a large primary suite, full finished lower level with bedroom, & family room. The living floor has high ceilings, oversized windows, and sliders to the new back deck, allowing for indoor/outdoor living. Large open kitchen with updated quartz countertops, lighting ,and stainless appliance package. Huge dining room with design-forward lighting and fireplace. Charming front porch, 2.5 car garage and a few steps from the 606. Open this Saturday March 29th from 2p to 5pm and Sunday March 30th from 11a to 2p. See additional info for FAQ sheet",
                                user1,
                                emptyImageList,
                                emptyUserList);

                Property property8 = new Property(
                                "2317 W Ohio St",
                                949000.00,
                                "Chicago, IL 60612",
                                3000,
                                "Fully gut-rehabbed in 2022, this 4 bedroom, 3.1 bath, 3000 SQFT single family home ideally located in Chicago's Ukrainian Village neighborhood. Flooded with natural light, the main level's open floor plan flows seamlessly and creates the ideal space for both entertaining and everyday living. The eat-in kitchen features an expansive center island with breakfast bar, Bosch appliances, and copious cabinet and counter space. A family room off the kitchen leads to a back deck and the backyard, ideal for grilling, relaxing, and dining al fresco. Upstairs on the second floor, three bedrooms, 2 full baths, and a laundry room are conveniently located on the same level. The lower level has a recreation room, a 4th bedroom, and a full bath. An all brick 2-car garage with roof deck was added to the property in 2022 and provides an urban oasis for relaxation and entertainment purposes. Ideally located near all Ukrainian Village has to offer and a short ride to the vibrant West Loop area. Close to public transportation and one block from Mitchell School.",
                                user2,
                                emptyImageList,
                                emptyUserList);

                Property property9 = new Property(
                                "1701 N Dayton St",
                                4750000.00,
                                "Chicago, IL 60614",
                                8000,
                                "Stunning LG custom-built single-family home on an extra wide 36.5' lot on a quiet stretch of Dayton in the heart of Lincoln Park. The classic red brick and limestone facade with bluestone steps to a carved walnut door welcomes you into this sweeping home that offers relaxed elegance with exceptional millwork, timeless finishes, and a thoughtful floor plan.    The elevated entryway features an oval tray ceiling and a symmetrical inlaid floor medallion inviting you into the gracious living room with a marble fireplace and adjoining ample dining room and butler's wet bar complete with a polished stainless steel sink, lined silver drawers, and a spacious walk-in pantry. The main gathering space of the first floor is the gourmet kitchen, boasting an angled coffered ceiling, double islands with Shaws' Farmhouse sinks, a mirrored Sub Zero refrigerator, and top-of-the-line Wolf appliances, including a wall oven, warming drawer, and side-by-side double oven with a griddle. Built-ins abound, plus a breakfast banquette that offers ample space for dining. The adjacent family room features a coffered ceiling and a grand oversized marble fireplace, flanked by paneled bookcases and more built-ins for the reading area and workstation. French doors with seeded leaded glass window transoms lead to a bluestone patio with a seating area and built-in DCS grill, providing a seamless transition to outdoor entertaining on the full-width 640 sqft Trex deck with a pergola. and a generously sized family room featuring an oversized marble fireplace and reading area. The first-floor features hickory, hand-scraped, and beveled hardwood floors, extensive millwork, crown molding, and 8 1/2\" baseboard trim, which continues throughout.    The second floor is dedicated to comfort and luxury with a primary suite that includes a marble fireplace, built-ins, and an opulent double sink marble bath with a separate built-in vanity area, featuring a separate shower and tub. There are a dreamy FOUR spacious closets, including one off the bath, one with a built-in island, and two adjoining shoe and cedar closets, providing incredible storage. The additional bedrooms are thoughtfully designed, with an en suite west-facing bedroom, a spacious middle bedroom that can be converted to an en suite, and a well-equipped laundry room featuring Whirlpool Duet appliances and a whirlpool utility sink for hand washables.    Who doesn't love a good sunset? This penthouse is made for you with incredible natural light and treetop views entertainment ready with a rooftop Trex terrace complete with a fireplace, sound, and irrigation systems, a butler's kitchen featuring a Sub-Zero refrigerator and freezer, and a Bosch dishwasher and your four-stop elevator so your guests will arrive in style. Two additional bedrooms, including a craft room, share a hall bath.    The expansive lower level is an entertainer's paradise with a home theater and a massive family room featuring a coffered ceiling, built-ins, and wall moldings. A 300-bottle wine cellar with accent lighting and a refrigerator complement the space, along with a full bathroom and laundry facilities. An en suite exercise room/bedroom with a large walk-in closet completes this remarkable lower level.    The second and third floors feature wide plank white oak flooring. The home is equipped with a smart home system, Lutron lighting, and a Keyth security system, all seamlessly integrated for modern living. The zoned heating and cooling systems, including radiant heated floors and snow-melt steps and walkways, ensure year-round comfort and convenience. The attached three-car garage features a heated epoxy floor.    This home is truly a masterpiece, offering luxury, functionality, and an enviable lifestyle in one of Chicago's most sought-after neighborhoods. Don't miss the opportunity to make this extraordinary residence your own!",
                                user3,
                                emptyImageList,
                                emptyUserList);

                Property property10 = new Property(
                                "334 N Jefferson St UNIT D",
                                925000.00,
                                "Chicago, IL 60661",
                                2600,
                                "This rare corner townhome in Kinzie Station offers 3 bedrooms and 3 baths, showcasing a renovated kitchen and an open-concept design. With a bright southeast exposure, natural light pours into the home, complemented by multiple private outdoor spaces with stunning city views.    The main level features a marble entryway and a versatile bedroom with an ensuite bath-ideal for a guest room, home office, or playroom. Upstairs, the primary suite boasts a walk-in closet and a beautifully updated bathroom with dual sinks. A second bedroom, a refreshed bathroom, and a laundry area complete this floor.    The sun-drenched third level highlights a spacious living room with a gas fireplace, elegant Brazilian Cherry hardwood floors, a dedicated dining area, and a balcony. The sleek white kitchen is equipped with a Viking stove, Sub-Zero refrigerator, expansive countertops, and a large breakfast bar.    On the fourth floor, a generous family room with a wet bar opens to an enormous private roof deck, complete with a gas line for grilling and built-in surround sound-perfect for entertaining or unwinding. Flexible spaces throughout the home allow for an office or a potential fourth bedroom. The property also includes a fenced yard and access to a private association parking lot, ideal for outdoor activities.    Residents enjoy access to amenities in the adjacent condo building, including a newly updated gym, security guard service, and secure package delivery. With FOUR parking spaces (garage, driveway, and two in the adjacent lot), this home offers both convenience and luxury. Experience peaceful, residential living just moments from the Loop, River North, and Fulton Market, with easy access to dining, shopping, parks, and the 90/94 expressway.",
                                user1,
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

                property1.addToPropertyImages(image1);
                property2.addToPropertyImages(image2);
                property3.addToPropertyImages(image3);
                property4.addToPropertyImages(image4);
                property5.addToPropertyImages(image5);
                property6.addToPropertyImages(image6);
                property7.addToPropertyImages(image7);
                property8.addToPropertyImages(image8);
                property9.addToPropertyImages(image9);
                property10.addToPropertyImages(image10);

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

                //////////////////
                //// Favorites
                //////////////////
                //
                // Favorite favorite1 = new Favorite(user2, property1);
                // Favorite favorite2 = new Favorite(user2, property4);
                // Favorite favorite3 = new Favorite(user2, property3);
                // Favorite favorite4 = new Favorite(user2, property6);
                // Favorite favorite5 = new Favorite(user4, property1);
                // favoritesRepository.save(favorite1);
                // favoritesRepository.save(favorite2);
                // favoritesRepository.save(favorite3);
                // favoritesRepository.save(favorite4);
                // favoritesRepository.save(favorite5);

        }

}
