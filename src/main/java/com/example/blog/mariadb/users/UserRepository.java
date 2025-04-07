package com.example.blog.mariadb.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // You can add custom queries here if necessary
    User findByUsername(String username);

    User findByEmail(String email);
    List<User> findByUsernameAndPassword(String username, String password);

}
