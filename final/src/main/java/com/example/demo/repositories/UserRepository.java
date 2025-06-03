package com.example.demo.repositories;

import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    List<User> findByAgent(User user);

    Arrays findByAgentIsNull();

    User findByFirstName(String name);

    List<User> findAllByOrderByLastNameAsc();
}
