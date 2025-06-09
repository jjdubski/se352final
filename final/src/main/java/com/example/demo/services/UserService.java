package com.example.demo.services;

import com.example.demo.entities.Message;
import com.example.demo.entities.Property;
import com.example.demo.entities.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    @PreAuthorize("isAuthenticated()")
    void prepareDashboardModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void prepareProfileModel(Model model);

    @PreAuthorize("isAuthenticated()")
    void updateUserProfile(User updatedUser, String password, List<Long> addIds, List<Long> removeIds);

    @PreAuthorize("hasRole('ADMIN')")
    List<User> getAllUsers();

    String storeProfilePicture(Long userId, MultipartFile file);

    User registerNewUser(User user, List<String> roleNames);

    void updateUser(User savedUser);

    @PreAuthorize("isAuthenticated()")
    User getCurrentUser();

    List<Message> findMessagesForUser(User user);

    @PreAuthorize("isAuthenticated()")
    List<Property> getFavorites();

    User getUserById(Long id);

    void delete(String email);

    @PreAuthorize("hasRole('BUYER')")
    Message sendMessage(Message message);

    Message findMessage(Long id);

    void sendMessageReply(Message message, String reply);
}
