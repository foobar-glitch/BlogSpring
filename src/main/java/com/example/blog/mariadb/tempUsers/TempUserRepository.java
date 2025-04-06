package com.example.blog.mariadb.tempUsers;

import com.example.blog.mariadb.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TempUserRepository extends JpaRepository<TempUser, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    @Query(value = "SELECT * FROM users u WHERE u.username = :username AND u.password = SHA2(CONCAT(:password, UNHEX(:salt)), 256)", nativeQuery = true)
    List<User> findByUsernameAndPasswordAndSalt(String username, String password, String salt);
}
