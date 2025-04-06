package com.example.blog.mariadb.tempUsers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempUserController {

    @Autowired
    private TempUserService userService;

}
