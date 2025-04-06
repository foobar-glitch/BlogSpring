package com.example.blog.mariadb.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id).orElse(null);
    }

    @PostMapping("/users/create")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }


    @PostMapping("/login")
    public Optional<User> login(@RequestParam String username, @RequestParam String password){
        return userService.findByUsernamePassword(username, password);
    }

    @PostMapping("/register")
    public void register(@RequestParam String username,
                         @RequestParam String email,
                         @RequestParam String password,
                         @RequestParam String confirmPassword
                         ){
        if(!password.equals(confirmPassword)){
            System.out.println("Passwords do not match");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if(userService.getUserByUsername(username) != null){
            System.out.println("User already exists");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if(userService.getUserByEmail(email) != null){
            System.out.println("E-Mail already exists");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        userService.addUserWithPasswordSaltAndRole(username, email, password, "user");
    }

    @GetMapping("/register/validate")
    public void validateToken(@RequestParam String token){
        System.out.println(token);

    }

    @GetMapping("/authenticate")
    public String authenticate(){
        return "Hello";
    }
}