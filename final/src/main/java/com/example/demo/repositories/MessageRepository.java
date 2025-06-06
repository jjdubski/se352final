package com.example.demo.repositories;

import com.example.demo.entities.Message;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Long, Message> {
    List<Message> findByReceipent(User user);

    Optional<Message> findById(Long id);
}
