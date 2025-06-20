package com.example.demo.controllers;

import com.example.demo.dtos.ApiExceptionDto;
import com.example.demo.entities.*;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AuthService;
import com.example.demo.services.PropertyService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Controller
public class AppController {
    private final AuthService authService;
    private final UserService userService;
    private final PropertyService propertyService;
    private final UserRepository userRepository;

    @Autowired
    public AppController(AuthService authService, UserService userService, PropertyService propertyService,
            UserRepository userRepository) {
        this.authService = authService;
        this.userService = userService;
        this.propertyService = propertyService;
        this.userRepository = userRepository;
    }

    // ===== LANDING PAGE ======
    @GetMapping({ "/", "/index" })
    public String showIndex() {
        return "index";
    }

    // === REGISTRATION ===
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
            @RequestParam(value = "file", required = false) MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {

            List<String> roleNames = List.of("BUYER");
            user.setCreatedAt();
            User savedUser = userService.registerNewUser(user, roleNames);

            if (file != null && !file.isEmpty()) {
                String filename = userService.storeProfilePicture(savedUser.getId(), file);
                savedUser.setProfilePicture(filename);
                userService.updateUser(savedUser);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Registration successful.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "redirect:/register";
        }
    }

    // === LOGIN/LOGOUT ===
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") User user,
            HttpServletResponse response,
            Model model) {
        try {
            Cookie jwtCookie = authService.loginAndCreateJwtCookie(user);
            response.addCookie(jwtCookie);
            return "redirect:/dashboard";
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String logout(HttpServletResponse response, Model model) {
        authService.clearJwtCookie(response);
        model.addAttribute("successMessage", "You have been logged out successfully.");
        return "login";
    }

    // === DASHBOARD ====
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String showDashboard(Model model) {
        userService.prepareDashboardModel(model);
        return "dashboard";
    }

    // ===== PROFILE =======
    // view profile
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String showProfile(Model model) {
        userService.prepareProfileModel(model);
        return "profile";
    }

    @GetMapping("/profile/edit")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String editProfile(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "editProfile";
    }

    // edit profile
    @PostMapping("/profile/edit")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String updateSettings(@ModelAttribute("user") User updatedUser,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) List<Long> addIds,
            @RequestParam(required = false) List<Long> removeIds,
            @RequestParam(value = "file", required = false) MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            User actualUser = userService.getCurrentUser();

            actualUser.setFirstName(updatedUser.getFirstName());
            actualUser.setLastName(updatedUser.getLastName());
            actualUser.setEmail(updatedUser.getEmail());

            userService.updateUserProfile(actualUser, password, addIds, removeIds);

            if (file != null && !file.isEmpty()) {
                String filename = userService.storeProfilePicture(actualUser.getId(), file);
                actualUser.setProfilePicture(filename);
                userService.updateUser(actualUser);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update account: " + ex.getMessage());
        }
        return "redirect:/profile/edit";
    }

    // === PROFILE PICTURE UPLOAD ===
    @PostMapping("/users/{id}/upload-profile-picture")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String uploadProfilePicture(@PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            String filename = userService.storeProfilePicture(id, file);
            redirectAttributes.addFlashAttribute("message", "Profile picture uploaded: " + filename);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    @GetMapping("/profile-pictures/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveProfilePicture(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/profile-pictures/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ===== USERS ======

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getAllUser(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("All-Users", users);
        return "allUsers";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getAllUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("Users", user);
        return "users";
    }

    @GetMapping("/user/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String addUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "addUser";
    }

    @PostMapping("/user/delete/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String deleteUser(@PathVariable String email,
            RedirectAttributes redirectAttributes) {
        try {
            userService.delete(email);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "User deletion failed:" + e.getMessage());
        }
        return "redirect:/users/admin";
    }

    // ====PROPERTIES====

    // property pic upload
    @PostMapping("/property/{id}/upload-property-picture")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String uploadPropertyPicture(@PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            String filename = propertyService.storePropertyPicture(id, file);
            redirectAttributes.addFlashAttribute("message", "Property picture uploaded: " + filename);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/property";
    }

    // property pic deletion
    @PostMapping("/property/{id}/delete-property-picture")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String deleteProperyPicture(@PathVariable Long id,
            @RequestParam("image") Image propertyImage,
            RedirectAttributes redirectAttributes) {
        try {
            String filename = propertyService.deletePropertyImage(id, propertyImage);
            redirectAttributes.addFlashAttribute("message", "Property picture uploaded: " + filename);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/property";
    }

    @GetMapping("/property-pictures/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> servePropertyPicture(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/property-pictures/").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // manage property
    @GetMapping("/properties/manage")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String manageProperties(Model model) {
        User agent = userService.getCurrentUser();
        model.addAttribute("properties", propertyService.getPropertiesForCurrentAgent(agent));
        return "manageListings";
    }

    @PostMapping("/properties/delete/{id}")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String deleteProperty(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            propertyService.deleteProperty(id);
            redirectAttributes.addFlashAttribute("successMessage", "Property deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Property deletion failed: " + e.getMessage());
        }
        return "redirect:/properties/manage";
    }

    // edit property page
    @GetMapping("/properties/edit/{id}")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String editPropertyPage(@PathVariable Long id, Model model) {
        try {
            Property property = propertyService.findPropertyById(id);
            model.addAttribute("property", property);
            return "editListing";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Property not found: " + e.getMessage());
            return "redirect:/properties/manage";
        }
    }

    // edit property
    @PostMapping("/properties/edit/{id}")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String editProperties(@PathVariable Long id,
            @ModelAttribute("property") Property updatedProperty,
            @RequestParam(value = "file", required = false) List<MultipartFile> files,
            RedirectAttributes redirectAttributes) {
        try {
            Property property = propertyService.findPropertyById(id);
            property = propertyService.editProperty(property, updatedProperty);

            redirectAttributes.addFlashAttribute("successMessage", "Property edited successfully.");
            return "redirect:/properties/manage";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Property edit failed: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/properties/edit/{id}";
        }
    }

    // add new property page
    @GetMapping("/properties/add")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String addNewPropertyPage(Model model) {
        Property property = new Property();
        model.addAttribute("property", property);
        return "addListing";
    }

    // add new property
    @PostMapping("/properties/add")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String addNewProperty(@ModelAttribute("property") Property property,
            // @RequestParam(value = "file", required = false) List<MultipartFile> files,
            RedirectAttributes redirectAttributes) {

        try {

            User agent = userService.getCurrentUser();
            Property savedProperty = propertyService.addNewProperty(property, agent);

            redirectAttributes.addFlashAttribute("successMessage", "Property added successfully.");
            return "redirect:/properties/manage";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Property registration failed: " + e.getMessage());
            return "redirect:/properties/add";
        }
    }

    // browse properties
    @GetMapping("/properties/list")
    @PreAuthorize("hasAnyRole('BUYER')")
    public String browseProperties(Model model) {
        model.addAttribute("properties", propertyService.getAllProperties());
        return "properties";
    }

    // view details
    @GetMapping("/properties/view/{id}")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT')")
    public String viewDetails(@PathVariable Long id, Model model) {
        // Property property = propertyService.getProperty(id);
        // model.addAttribute("property", property);
        model.addAttribute("property", propertyService.findPropertyById(id));
        return "property";
    }

    // image viewer
    @GetMapping("/properties/view/{id}/images")
    @PreAuthorize("hasAnyRole('BUYER')")
    public String viewImages(@PathVariable Long id, Model model) {
        // List<Image> images = propertyService.getImages(id);
        // model.addAttribute("Images", images);
        // return "images";
        List<Image> propertyImages = propertyService.getImagesForProperty(id);
        model.addAttribute("images", propertyImages);
        return "images"; // UPDATE with actual template
    }

    // ===== FAVORITES ======

    @GetMapping("/favorites")
    @PreAuthorize("hasAnyRole('BUYER')")
    public String favorites(Model model) {
        List<Property> favorites = userService.getFavorites();
       model.addAttribute("favorites", favorites);
        return "favorites";
    }

    // =====MESSAGES=====

    // all messages
    @GetMapping("/messages/agent")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String allMessagesAgent(Model model) {
        User user = userService.getCurrentUser();
        List<Message> messages = userService.findMessagesForAgent(user);
        model.addAttribute("user", user);
        model.addAttribute("messages", messages);
        return "messages";
    }

    @GetMapping("/messages/buyer")
    @PreAuthorize("hasAnyRole('BUYER')")
    public String allMessagesBuyer(Model model) {
        User user = userService.getCurrentUser();
        List<Message> messages = userService.findMessagesForBuyer(user);
        model.addAttribute("user", user);
        model.addAttribute("messages", messages);
        return "messages";
    }

    @PostMapping("/messages")
    @PreAuthorize("hasAnyRole('BUYER')")
    public String sendMessage(@ModelAttribute("message") Message message,
            @RequestParam("property") Property property,
            RedirectAttributes redirectAttributes) {
        try {
            User sender = userService.getCurrentUser();
            message.setSender(sender);
            message.setRecipient(property.getListingAgent());
            message.setProperty(property);
            userService.sendMessage(message);

            redirectAttributes.addFlashAttribute("successMessage", "Message sent successfully.");
            return "redirect:/messages/buyer";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to send message: " + e.getMessage());
            return "redirect:/properties/view/" + property.getId();
        }
    }

    @PostMapping("/messages/reply")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String replyToMessage(@RequestParam("messageId") Long messageId,
            @RequestParam("reply") String reply,
            RedirectAttributes redirectAttributes) {
        try {
            Message message = userService.findMessage(messageId);
            userService.sendMessageReply(message, reply);

            redirectAttributes.addFlashAttribute("successMessage", "Reply sent successfully.");
            return "redirect:/messages/agent";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to send reply: " + e.getMessage());
            return "redirect:/messages/" + messageId;
        }
    }

    // single message
    @GetMapping("/messages/{id}")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String singleMessage(@PathVariable Long id, Model model) {
        Message message = userService.findMessage(id);
        model.addAttribute("message", message);
        return "message";
    }

    // ====== CREATE =======

    @GetMapping("/users/admin/create-agent")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String createAgent(Model model) {
        User agent = new User();
        model.addAttribute("agent", agent);
        return "createAgent";
    }

    @PostMapping("/users/admin/create-agent")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String registerAgent(@ModelAttribute("agent") User agent,
            @RequestParam(value = "file", required = false) MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            List<String> roleNames = List.of("AGENT");
            agent.setCreatedAt();
            User savedUser = userService.registerNewUser(agent, roleNames);

            if (file != null && !file.isEmpty()) {
                String filename = userService.storeProfilePicture(savedUser.getId(), file);
                savedUser.setProfilePicture(filename);
                userService.updateUser(savedUser);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Registration successful.");
            return "redirect:/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "redirect:/users/admin/create-agent";
        }
    }

    // ======= MANAGE ========

    @GetMapping("/users/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "allUsers";
    }

    /////////////////////////////////////////////
    // Exception Handling
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleNotFoundException(
            BadCredentialsException ex, HttpServletRequest request) {
        ApiExceptionDto apiExceptionDto = new ApiExceptionDto(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI(),
                ex.getClass().getSimpleName());
        return new ResponseEntity<>(apiExceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleNotFoundException(
            Exception ex, HttpServletRequest request) {
        ApiExceptionDto apiExceptionDto = new ApiExceptionDto(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                ex.getClass().getSimpleName());
        return new ResponseEntity<>(apiExceptionDto, HttpStatus.BAD_REQUEST);
    }
}
