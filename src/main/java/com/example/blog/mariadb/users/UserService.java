package com.example.blog.mariadb.users;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.blog.HashingHelper.hashPasswordWithSalt;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;


    @Query("SELECT u FROM User u")
    public List<UserTable> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserTable> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public UserTable getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserTable getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public UserTable saveUser(UserTable user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    public Optional<UserTable> findByUsernamePassword(String username, String password) {
        UserTable user = userRepository.findByUsername(username);
        if (user == null) {
            return Optional.empty();
        }
        String passwordHash = hashPasswordWithSalt(password, user.getSalt());
        List<UserTable> users = userRepository.findByUsernameAndPassword(username, passwordHash);
        return (users != null && !users.isEmpty()) ? Optional.of(users.get(0)) : Optional.empty();
    }


}