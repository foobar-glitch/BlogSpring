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

import static com.example.blog.mariadb.tempUsers.HashingHelper.*;

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
