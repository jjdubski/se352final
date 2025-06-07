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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AppController {
    private final AuthService authService;
    private final UserService userService;
    private final PropertyService propertyService;
    private final UserRepository userRepository;

    @Autowired
    public AppController(AuthService authService, UserService userService, PropertyService propertyService, UserRepository userRepository) {
        this.authService = authService;
        this.userService = userService;
        this.propertyService = propertyService;
        this.userRepository = userRepository;
    }

    // ===== LANDING PAGE ======
    @GetMapping({"/", "/index"})
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
            // First, register the user (this will assign them an ID)

            List<String> roleNames = List.of("BUYER");
            user.setCreatedAt();
            User savedUser = userService.registerNewUser(user, roleNames);


            // Then, store the profile picture (if uploaded) and update the user record
            if (file != null && !file.isEmpty()) {
                String filename = userService.storeProfilePicture(savedUser.getId(), file);
                savedUser.setProfilePicture(filename);
                // Save again to persist the filename
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
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String logout(HttpServletResponse response) {
        authService.clearJwtCookie(response);
        return "redirect:/login";
    }

    // === DASHBOARD ====
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String showDashboard(Model model) {
        userService.prepareDashboardModel(model);
        return "dashboard";
    }

    //===== PROFILE =======
    //view profile
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String showProfile(Model model) {
        userService.prepareProfileModel(model);
        return "profile";
    }

    //edit profile
    @PostMapping("/profile/edit")
    @PreAuthorize("hasAnyRole('BUYER', 'AGENT', 'ADMIN')")
    public String updateSettings(@ModelAttribute("user") User updatedUser,
                                 @RequestParam(required = false) String password,
                                 @RequestParam(required = false) List<Long> addIds,
                                 @RequestParam(required = false) List<Long> removeIds,
                                 @RequestParam(value = "file", required = false) MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Look up the real user so we get the correct ID
            User actualUser = userService.getCurrentUser();

            // Copy updates from form-bound user
            actualUser.setFirstName(updatedUser.getFirstName());
            actualUser.setLastName(updatedUser.getLastName());
            actualUser.setEmail(updatedUser.getEmail());

            userService.updateUserProfile(actualUser, password, addIds, removeIds);

            // Save profile picture if provided
            if (file != null && !file.isEmpty()) {
                String filename = userService.storeProfilePicture(actualUser.getId(), file);
                actualUser.setProfilePicture(filename);
                userService.updateUser(actualUser);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Account updated successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update account: " + ex.getMessage());
        }
        return "redirect:/settings";
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

    //===== USERS ======

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getAllUser(Model model){
        List<User> users = userService.getAllUsers();
        model.addAttribute("All-Users", users);
        return "allUsers";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getAllUser(@PathVariable Long id, Model model){
        User user = userService.getUserById(id);
        model.addAttribute("Users", user);
        return "users";
    }

    @PutMapping("/user/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseBody
    public String updateUser(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "addUser";
    }

    @DeleteMapping("/user/delete/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String deleteUser(@PathVariable String email){
        userService.delete(email);
        return "delete";
    }

    //====PROPERTIES====

    // property pic upload
    @PostMapping("/property/{id}/upload-property-picture")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String uploadPropertyPicture(@PathVariable Long id,
                                        @RequestParam("file") MultipartFile file,
                                        RedirectAttributes redirectAttributes) {
        try {
            String filename = userService.storeProfilePicture(id, file);
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

    //manage property
    @GetMapping("/properties/manage")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String manageProperties(Model model){
        User agent = userService.getCurrentUser();
        model.addAttribute("properties",propertyService.getPropertiesForCurrentAgent(agent));
        return "manageListings";
    }

    //edit property
    @PostMapping("/properties/edit/{id}")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String editProperties(@PathVariable Long id,
                                 @ModelAttribute("property") Property updatedProperty,
                                 RedirectAttributes redirectAttributes) {
        try {
            Property property = propertyService.findPropertyById(id);
            property = propertyService.editProperty(property, updatedProperty);

            redirectAttributes.addFlashAttribute("successMessage", "Property edited successfully.");
            return "redirect:/properties/manage";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Property edit failed: " + e.getMessage());
            return "redirect:/properties/edit/{id}";
        }
    }

    //add new property
    @PutMapping("properties/add")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String addNewProperty(@ModelAttribute("property") Property property,
                                 @RequestParam(value = "file", required = false) List<MultipartFile> files,
                                 RedirectAttributes redirectAttributes){

        try{

            Property savedProperty = propertyService.addNewProperty(property,files);

            redirectAttributes.addFlashAttribute("successMessage", "Property added successfully.");
            return "redirect:/properties/manage";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Property registration failed: " + e.getMessage());
            return "redirect:/properties/add";
        }
    }

    //browse properties
    @GetMapping("/properties/list")
    @PreAuthorize("hasAnyRole('BUYER')")
    public String browseProperties(Model model){
        model.addAttribute("properties", propertyService.getAllProperties());
        return "properties";
    }

    //view details
    @GetMapping("/properties/{id}")
    @PreAuthorize("hasAnyRole('BUYER')")
    public String viewDetails(@PathVariable Long id, Model model){
//         Property property = propertyService.getProperty(id);
//         model.addAttribute("property", property);
        model.addAttribute("property",propertyService.findPropertyById(id));
        return "property";
    }

    //image viewer
    @GetMapping("/properties/{id}/images")
    @PreAuthorize("hasAnyRole('BUYER')")
    public String viewImages(@PathVariable Long id, Model model){
//         List<Image> images = propertyService.getImages(id);
//         model.addAttribute("Images", images);
//         return "images";
        List<Image> propertyImages = propertyService.getImagesForProperty(id);
        model.addAttribute("images", propertyImages);
        return "images";   //UPDATE with actual template
    }

    //===== FAVORITES ======

    @GetMapping("/favorites")
    @PreAuthorize("hasAnyRole('BUYER')")
    public String favorites(Model model){
        List<Property> properties = userService.getFavorites();
        model.addAttribute("favorites", properties);
        return "favorites";
    }

    //=====MESSAGES=====

    //all messages
    @GetMapping("/messages")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String allMessages(Model model){
        User user = userService.getCurrentUser();
        List<Message> messages = userService.findMessagesForUser(user);
        model.addAttribute("messages", messages);
        return "messages";
    }

    //single message
    @GetMapping("/message/{id}")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String singleMessage(@PathVariable Long id, Model model){
        Message message = userService.findMessage(id);
        model.addAttribute("message", message);
        return "message";
    }

    //====== CREATE =======

    @GetMapping("/users/admin/create-agent")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String createAgent(Model model){
        User agent = new User();
        model.addAttribute("Agent", agent);
        return "createAgent";
    }

    //======= MANAGE ========

    @GetMapping("/users/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String manageUsers(Model model){
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
                ex.getClass().getSimpleName()
        );
        return new ResponseEntity<>(apiExceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleNotFoundException(
            Exception ex, HttpServletRequest request) {
        ApiExceptionDto apiExceptionDto = new ApiExceptionDto(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                ex.getClass().getSimpleName()
        );
        return new ResponseEntity<>(apiExceptionDto, HttpStatus.BAD_REQUEST);
    }
}
