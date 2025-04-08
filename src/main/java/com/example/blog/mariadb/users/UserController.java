package com.example.blog.mariadb.users;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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


    @GetMapping("/set-token-cookie")
    public String setTokenCookie(HttpServletResponse response) {
        // Generate a random token using UUID
        String token = UUID.randomUUID().toString();

        // Create a cookie to store the token
        Cookie cookie = new Cookie("token", token);

        // Set the cookie's max age (in seconds)
        cookie.setMaxAge(60 * 60); // 1 hour

        // Optionally set the cookie's path
        cookie.setPath("/");

        // Set the cookie to be accessible over HTTP only (to prevent JavaScript access)
        cookie.setHttpOnly(true);

        // Add the cookie to the response
        response.addCookie(cookie);

        return "Random token cookie has been set!";
    }

    @GetMapping("/read-cookie")
    public String readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return "Cookie value: " + cookie.getValue();
                }
            }
        }
        return "Cookie not found!";
    }




    @GetMapping("/authenticate")
    public String authenticate(){
        return "Hello";
    }
}