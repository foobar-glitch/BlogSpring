package com.example.blog.mariadb.tempUsers;

import com.example.blog.mariadb.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TempUserController {

    @Autowired
    private TempUserService tempUserService;
    @Autowired
    private UserService userService;


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

}
