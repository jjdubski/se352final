package com.example.demo.controllers;

import com.example.demo.dtos.ApiExceptionDto;
import com.example.demo.entities.Image;
import com.example.demo.entities.User;
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

    @Autowired
    public AppController(AuthService authService, UserService userService, PropertyService propertyService) {
        this.authService = authService;
        this.userService = userService;
        this.propertyService = propertyService;
    }

    // landing page
    @GetMapping({"/", "/index"})
    public String showIndex() {
        return "index";
    }

    // register page
    @PostMapping
    @ResponseBody
    public String register(){
        return "user registered";
    }

    
    // === LOGIN ===
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
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAnyRole('USER', 'AGENT', 'ADMIN')")
    public String logout(HttpServletResponse response) {
        authService.clearJwtCookie(response);
        return "redirect:/login";
    }

    // === DASHBOARD ====
    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('USER', 'AGENT', 'ADMIN')")
    public String showDashboard(Model model) {
        userService.prepareDashboardModel(model);
        return "dashboard";
    }

    //===== PROFILE =======
    //view profile
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'AGENT', 'ADMIN')")
    public String showProfile(Model model) {
        userService.prepareProfileModel(model);
        return "profile";
    }

    //edit profile
    @PostMapping("/profile/edit")
    @PreAuthorize("hasAnyRole('USER', 'AGENT', 'ADMIN')")
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
    @PreAuthorize("hasAnyRole('USER', 'AGENT', 'ADMIN')")
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

    //====PROPERTIES====

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
    public String editProperties(@PathVariable Long id, Model model) {
            return "property edited";
    }

    //add new property
    @PutMapping("properties/add")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String addNewProperty(){
        return "added new property";
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
        model.addAttribute("property",propertyService.findPropertyById(id));
        return "property";
    }

    //image viewer
    @GetMapping("/properties/{id}/images")
    @PreAuthorize("hasAnyRole('BUYER')")
    public String viewImages(@PathVariable Long id, Model model){
        List<Image> propertyImages = propertyService.getImagesForProperty(id);
        model.addAttribute("images", propertyImages);
        return "images";   //UPDATE with actual template
    }

    //===== FAVORITES ======

    @GetMapping("/favorties")
    @PreAuthorize("hasAnyRole('BUYER')")
    public String favorites(){
        return "favorties";
    }

    //=====MESSAGES=====

    //all messages
    @GetMapping("/messages")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String allMessages(){
        return "messages";
    }

    //single message
    @GetMapping("/message/{id}")
    @PreAuthorize("hasAnyRole('AGENT')")
    public String singleMessage(@PathVariable String id){
        return "single message";
    }

    //====== CREATE AND MANAGE =======

    @PostMapping("/users/admin/create-agent")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String createAgent(){
        return "agent created";
    }

    @PostMapping("/users/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String manageUsers(){
        return "users managed";
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
