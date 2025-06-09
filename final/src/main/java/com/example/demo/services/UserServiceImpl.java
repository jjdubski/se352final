package com.example.demo.services;

import com.example.demo.exceptions.InvalidMessageParameterException;
import com.example.demo.exceptions.MessageNotFoundException;
import com.example.demo.repositories.*;
import com.example.demo.utils.CurrentUserContext;

import jakarta.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

//import com.example.demo.entities.Role;
import com.example.demo.entities.*;

import java.beans.Transient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageRepository messageRepository;
    private final FavoriteRepository favoriteRepository;

    public UserServiceImpl(UserRepository userRepository, PropertyRepository propertyRepository, RoleRepository roleRepository,
            MessageRepository messageRepository, PasswordEncoder passwordEncoder, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.roleRepository = roleRepository;
        this.messageRepository = messageRepository;
        this.passwordEncoder = passwordEncoder;
        this.favoriteRepository = favoriteRepository;
    }

    private CurrentUserContext getCurrentUserContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        return new CurrentUserContext(user, auth);
    }

    @Override
    public void prepareDashboardModel(Model model) {
        CurrentUserContext context = getCurrentUserContext();
        User user = context.user();
        List<Message> buyermessages = findMessagesForBuyer(user);
        List<Message> agentmessages = findMessagesForAgent(user);
        List<Favorite> favorites = favoriteRepository.findByBuyer(user);

        model.addAttribute("user", user);
        model.addAttribute("authorization", context.auth());
        model.addAttribute("buyermessages", buyermessages);
        model.addAttribute("favorites", favorites);
        model.addAttribute("agentmessages", agentmessages);
    }

    @Override
    public void prepareProfileModel(Model model) {
        model.addAttribute("user", getCurrentUserContext().user());
    }

    @Override
    public void updateUserProfile(User updatedUser, String password, List<Long> addIds, List<Long> removeIds) {
        User user = getCurrentUserContext().user();

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (addIds != null) {
            for (Long empId : addIds) {
                User emp = userRepository.findById(empId).orElseThrow();
                emp.setAgent(user);
                userRepository.save(emp);
            }
        }

        if (removeIds != null) {
            for (Long empId : removeIds) {
                User emp = userRepository.findById(empId).orElseThrow();
                emp.setAgent(null);
                userRepository.save(emp);
            }
        }

        userRepository.save(user);
    }

    @Override
    public User registerNewUser(User user, List<String> roleNames) {
        Set<Role> roles = roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    public String storeProfilePicture(Long userId, MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Resolve absolute path relative to the project directory
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "profile-pictures");
            Files.createDirectories(uploadPath); // Ensure path exists

            // Locate user and remove previous image (if any)
            User user = userRepository.findById(userId).orElseThrow();

            if (user.getProfilePicture() != null && !user.getProfilePicture().equals("default.jpg")) {
                Path oldPath = uploadPath.resolve(user.getProfilePicture());
                Files.deleteIfExists(oldPath);
            }

            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());

            // Save to user
            user.setProfilePicture(filename);
            userRepository.save(user);

            return filename;

        } catch (IOException ex) {
            System.out.println("Failed to save file: " + ex.getMessage());
            throw new RuntimeException("Failed to store profile picture", ex);
        }
    }

    @Override
    public void updateUser(User savedUser) {
        userRepository.save(savedUser);
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        if (email == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        return userRepository.findByEmail(email);
    }

    @Override
    public List<Message> findMessagesForAgent(User user) {
        return messageRepository.findByRecipient(user);
    }

    @Override
    public List<Message> findMessagesForBuyer(User user){
        return messageRepository.findBySender(user);
    }



    @Override
    public Message sendMessage(Message message) {
        User sender = getCurrentUserContext().user();
        Optional<Property> propertyOpt = propertyRepository.findById(message.getProperty().getId());
        if (propertyOpt.isEmpty()) {
            throw new RuntimeException("Property not found with id: " + message.getProperty().getId());
        }
        message.setSender(sender);
        message.setProperty(propertyOpt.get());
        return messageRepository.save(message);
    }

    @Override
    public Message findMessage(Long id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isEmpty()) {
            throw new MessageNotFoundException("No message found with id: " + id);
        }
        return message.get();
    }

    @Override
    public void sendMessageReply(Message message, String reply) {
        message.setReply(reply);
        if (message.getReply() == null || message.getReply().isBlank()) {
            throw new InvalidMessageParameterException("Reply must contain text.");
        }
        messageRepository.save(message);
    }

    @Override
    public List<Property> getFavorites() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        if (email == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        User user = userRepository.findByEmail(email);
        List<Favorite> favorites = favoriteRepository.findByBuyer(user);
        List<Property> properties = new ArrayList<>();
        for (Favorite favorite : favorites){
            properties.add(favorite.getProperty());
        }
        return properties;
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.getReferenceById(id);
        return user;
    }

    @Transactional
    @Override
    public void delete(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        userRepository.delete(user);
    }

}
