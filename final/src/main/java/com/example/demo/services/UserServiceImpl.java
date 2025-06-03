package com.example.demo.services;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.utils.CurrentUserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.entities.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private CurrentUserContext getCurrentUserContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userRepository.findByFirstName(name);
        if(user == null){
            throw new UsernameNotFoundException("User not found" + name);
        }
        return new CurrentUserContext(user, auth);
    }

    @Override
    public void prepareDashboardModel(Model model) {
        CurrentUserContext context = getCurrentUserContext();
        model.addAttribute("user", context.user());
        model.addAttribute("authorization", context.auth());
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
            Files.createDirectories(uploadPath);  // Ensure path exists

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
        String name = auth.getName();
        if(name == null){
            throw new UsernameNotFoundException("User not found: " + name);
        }
        return userRepository.findByFirstName(name);
    }
}
