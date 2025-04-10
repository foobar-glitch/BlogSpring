package com.example.blog.mariadb.users;

import com.example.blog.mariadb.cookieTable.CookieTable;
import com.example.blog.mariadb.cookieTable.CookieTableService;
import com.example.blog.mariadb.resetTable.ResetTable;
import com.example.blog.mariadb.resetTable.ResetTableService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.example.blog.HashingHelper.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CookieTableService cookieTableService;

    @Autowired
    private ResetTableService resetTableService;

    @GetMapping("/users")
    public List<UserTable> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserTable getUser(@PathVariable Long id) {
        return userService.getUserById(id).orElse(null);
    }

    @PostMapping("/users/create")
    public UserTable createUser(@RequestBody UserTable user) {
        return userService.saveUser(user);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password,
                      HttpServletRequest request){
        Optional<UserTable> optionalUserTable= userService.findByUsernamePassword(username, password);
        if(optionalUserTable.isEmpty()){
            return new ResponseEntity<>("User and password dont match",HttpStatus.FORBIDDEN);
        }
        UserTable userTable = optionalUserTable.get();
        LocalDateTime currentTime = LocalDateTime.now();

        CookieTable cookieTable = new CookieTable();

        // hashed cookie Id
        String cookieId = readCookie(request);
        if(cookieId == null){
            System.out.println("No Cookie set");
            return new ResponseEntity<>("No Cookie set",HttpStatus.FORBIDDEN);
        }
        //If the entry already exists delete it
        Optional<CookieTable> optionalCookieTable = cookieTableService.findByCookieData(hashValue(cookieId.getBytes()));
        optionalCookieTable.ifPresent(table -> cookieTableService.deleteById(table.getCookieId()));


        cookieTable.setUserId(userTable.getUserId());
        cookieTable.setCookieData(hashValue(cookieId.getBytes()));
        cookieTable.setCreatedAt(currentTime);
        cookieTable.setExpiredAt(currentTime.plusMinutes(10));

        cookieTableService.save(cookieTable);
        return new ResponseEntity<>("Login successful", HttpStatus.ACCEPTED);
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
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


    //Authenticate using cookie
    @GetMapping("/authenticate")
    public User authenticate(HttpServletRequest request){
        LocalDateTime currentTime = LocalDateTime.now();
        String cookie = readCookie(request);
        if(cookie == null){
            System.out.println("No Cookie Set");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Optional<CookieTable> optionalCookieTable = cookieTableService.findByCookieData(hashValue(cookie.getBytes()));
        if(optionalCookieTable.isEmpty()){
            System.out.println("Did not login");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        CookieTable cookieTable = optionalCookieTable.get();

        if(currentTime.isAfter(cookieTable.getExpiredAt())){
            cookieTableService.deleteById(cookieTable.getCookieId());
            System.out.println("Cookie expired");
            return null;
        }


        Optional<UserTable> optionalUserTable = userService.getUserById(cookieTable.getUserId());
        if(optionalUserTable.isEmpty()){
            System.out.println("Internal Error: User not found");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        UserTable userTable = optionalUserTable.get();
        return userTable.getUser();
    }

    @PostMapping("/forgot")
    public ResponseEntity<String> forgotUser(@RequestParam String email){
        LocalDateTime currentTime = LocalDateTime.now();
        UserTable userTable = userService.getUserByEmail(email);
        if(userTable == null){
            return new ResponseEntity<>("E-Mail not found", HttpStatus.OK);
        }

        String resetToken = UUID.randomUUID().toString();
        System.out.println("Forgot Password Token: " + resetToken);

        ResetTable resetTable = new ResetTable();
        resetTable.setUserId(userTable.getUserId());
        resetTable.setResetToken(hashValue(resetToken.getBytes()));
        resetTable.setCreatedAt(currentTime);
        resetTable.setExpiredAt(currentTime.plusHours(24));
        resetTableService.save(resetTable);

        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public  ResponseEntity<String> resetUser(@RequestParam String token, String password, String confirmPassword){
        if(!Objects.equals(password, confirmPassword)){
            return new ResponseEntity<>("Passwords dont match", HttpStatus.BAD_REQUEST);
        }
        Optional<ResetTable> optionalResetTable = resetTableService.findByResetToken(hashValue(token.getBytes()));
        if(optionalResetTable.isEmpty()){
            return new ResponseEntity<>("Token not Found", HttpStatus.FORBIDDEN);
        }
        ResetTable resetTable =optionalResetTable.get();
        Optional<UserTable> optionalUserTable = userService.getUserById(resetTable.getUserId());
        if(optionalUserTable.isEmpty()){
            return new ResponseEntity<>("UserID not found Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        UserTable userTable = optionalUserTable.get();
        String newSalt = generateRandomSalt();
        userTable.setSalt(newSalt);
        userTable.setPasswordHash(hashPasswordWithSalt(password, newSalt));

        // overwrite user
        userService.saveUser(userTable);
        resetTableService.deleteById(resetTable.getResetId());
        return new ResponseEntity<>("Password Reset", HttpStatus.OK);
    }

}