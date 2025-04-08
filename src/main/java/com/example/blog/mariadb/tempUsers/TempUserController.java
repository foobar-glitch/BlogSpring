package com.example.blog.mariadb.tempUsers;

import com.example.blog.mariadb.registerTable.RegisterTable;
import com.example.blog.mariadb.registerTable.RegisterTableRepository;
import com.example.blog.mariadb.users.UserTable;
import com.example.blog.mariadb.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.time.LocalDateTime;

import static com.example.blog.mariadb.tempUsers.HashingHelper.hashValue;

@RestController
public class TempUserController {

    @Autowired
    private TempUserService tempUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private RegisterTableRepository registerTableService;


    /**
     * Registers User to the temporary database
     */
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
        TempUser tempUser = tempUserService.getUserByUsername(username);
        if(tempUser != null){
            System.out.println("Temp user already registered delete user");
            tempUserService.deleteUser(tempUser.getTempUserId());
        }

        tempUserService.addUserWithPasswordSaltAndRole(username, email, password, "user");
    }


    @GetMapping("/register/validate")
    public void validateToken(@RequestParam String token){
        String hashedToken = hashValue(new BigInteger(token, 16).toByteArray());

        RegisterTable registerTable = registerTableService.findByRegisterToken(hashedToken);
        if(registerTable == null){
            System.out.println("This entry does not exist");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        //check table time...
        LocalDateTime currentTime = LocalDateTime.now();
        if(registerTable.getExpiredAt().isBefore(currentTime)){
            System.out.println("Register Entry expired");
            registerTableService.delete(registerTable);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // Inserting temp user to user
        TempUser tempUser = registerTable.getTempUser();
        UserTable user = new UserTable();
        user.setEmail(tempUser.getEmail());
        user.setRole(tempUser.getRole());
        user.setSalt(tempUser.getSalt());
        user.setUsername(tempUser.getUsername());
        user.setPasswordHash(tempUser.getPasswordHash());
        user.setCreatedAt(tempUser.getCreatedAt());
        user.setUpdatedAt(tempUser.getUpdatedAt());

        userService.saveUser(user);
        tempUserService.deleteUser(tempUser.getTempUserId());

    }


}
