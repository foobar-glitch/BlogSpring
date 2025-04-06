package com.example.blog.mariadb.users;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;


    @Query("SELECT u FROM User u")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    public Optional<User> findByUsernamePassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return Optional.empty();
        }
        List<User> users = userRepository.findByUsernameAndPasswordAndSalt(username, password, user.getSalt());
        return (users != null && !users.isEmpty()) ? Optional.of(users.get(0)) : Optional.empty();
    }


}