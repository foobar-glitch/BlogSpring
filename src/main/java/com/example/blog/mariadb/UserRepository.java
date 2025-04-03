package com.example.blog.mariadb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // You can add custom queries here if necessary
    User findByUsername(String username);

    @Query(value = "SELECT * FROM users u WHERE u.username = :username AND u.password = SHA2(CONCAT(:password, UNHEX(:salt)), 256)", nativeQuery = true)
    List<User> findByUsernameAndPasswordAndSalt(String username, String password, String salt);

}
