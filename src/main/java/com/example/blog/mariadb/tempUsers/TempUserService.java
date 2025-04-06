package com.example.blog.mariadb.tempUsers;

import com.example.blog.mariadb.users.User;
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
public class TempUserService {

    @Autowired
    private TempUserRepository userRepository;


    @Query("SELECT u FROM User u")
    public List<TempUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<TempUser> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public TempUser saveUser(TempUser user) {
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

    private static String generateRandomSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[8]; // 8 bytes = 64 bits
        secureRandom.nextBytes(salt); // Generate random bytes
        return bytesToHex(salt); // Convert bytes to hex string
    }

    // Helper method to convert bytes to a hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b)); // Convert each byte to a 2-digit hex value
        }
        return hexString.toString();
    }

    private String hashPasswordWithSalt(String password, String salt) {
        try {
            byte[] passwordBytes = password.getBytes();
            byte[] saltBytes = new BigInteger(salt, 16).toByteArray();
            // Concatenate password and salt
            byte[] passwordWithSalt = new byte[passwordBytes.length + saltBytes.length];

            // Copy the first array into the result
            System.arraycopy(passwordBytes, 0, passwordWithSalt, 0, passwordBytes.length);
            // Copy the second array into the result starting after the first array
            System.arraycopy(saltBytes, 0, passwordWithSalt, passwordBytes.length, saltBytes.length);

            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing
            byte[] hashBytes = digest.digest(passwordWithSalt);

            // Return the hashed password as a hex string
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public void addUserWithPasswordSaltAndRole(String username, String email, String password, String role) {
        String salt = generateRandomSalt();
        TempUser user = new TempUser();
        user.setUsername(username);
        user.setPasswordHash(hashPasswordWithSalt(password, salt));
        user.setSalt(salt);
        user.setEmail(email);
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }



}
