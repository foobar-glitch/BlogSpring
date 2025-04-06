package com.example.blog.mariadb.tempUsers;

import com.example.blog.mariadb.registerTable.RegisterTable;
import com.example.blog.mariadb.registerTable.RegisterTableRepository;
import com.example.blog.mariadb.users.User;
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
public class TempUserService {

    @Autowired
    private TempUserRepository tempUserRepository;

    @Autowired
    private RegisterTableRepository registerTableRepository;


    @Query("SELECT u FROM User u")
    public List<TempUser> getAllUsers() {
        return tempUserRepository.findAll();
    }

    public Optional<TempUser> getUserById(Long userId) {
        return tempUserRepository.findById(userId);
    }

    public TempUser getUserByUsername(String username) {
        return tempUserRepository.findByUsername(username);
    }

    public TempUser getUserByEmail(String email){
        return tempUserRepository.findByEmail(email);
    }

    public TempUser saveUser(TempUser user) {
        return tempUserRepository.save(user);
    }

    public void deleteUser(Long userId) {
        tempUserRepository.deleteById(userId);
    }


    public Optional<TempUser> findByUsernamePassword(String username, String password) {
        TempUser user = tempUserRepository.findByUsername(username);
        if (user == null) {
            return Optional.empty();
        }
        List<TempUser> users = tempUserRepository.findByUsernameAndPasswordAndSalt(username, password, user.getSalt());
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

        byte[] passwordBytes = password.getBytes();
        byte[] saltBytes = new BigInteger(salt, 16).toByteArray();
        // Concatenate password and salt
        byte[] passwordWithSalt = new byte[passwordBytes.length + saltBytes.length];

        // Copy the first array into the result
        System.arraycopy(passwordBytes, 0, passwordWithSalt, 0, passwordBytes.length);
        // Copy the second array into the result starting after the first array
        System.arraycopy(saltBytes, 0, passwordWithSalt, passwordBytes.length, saltBytes.length);

        return hashValue(passwordWithSalt);
    }

    private String hashValue(byte[] value) {
        // Create a MessageDigest instance for SHA-256
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            // Perform the hashing
            byte[] hashBytes = digest.digest(value);

            // Return the hashed password as a hex string
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUserWithPasswordSaltAndRole(String username, String email, String password, String role) {
        LocalDateTime currentTime = LocalDateTime.now();
        String salt = generateRandomSalt();

        TempUser user = new TempUser();
        user.setUsername(username);
        user.setPasswordHash(hashPasswordWithSalt(password, salt));
        user.setSalt(salt);
        user.setEmail(email);
        user.setRole(role);
        user.setCreatedAt(currentTime);
        user.setUpdatedAt(currentTime);
        tempUserRepository.save(user);

        RegisterTable registerTable = new RegisterTable();
        String randomRegisterToken = generateRandomSalt();
        System.out.println("Registertoken = " + randomRegisterToken);
        registerTable.setRegisterToken(hashValue(new BigInteger(randomRegisterToken, 16).toByteArray()));
        registerTable.setCreatedAt(currentTime);
        registerTable.setTempUser(user);
        //Expires in 24 hours
        registerTable.setExpiredAt(currentTime.plusHours(24));
        registerTableRepository.save(registerTable);



    }



}
