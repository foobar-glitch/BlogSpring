package com.example.blog.mariadb.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserTable, Long> {
    // You can add custom queries here if necessary
    UserTable findByUsername(String username);

    UserTable findByEmail(String email);
    //password hash (using salt)
    List<UserTable> findByUsernameAndPassword(String username, String password);

}
