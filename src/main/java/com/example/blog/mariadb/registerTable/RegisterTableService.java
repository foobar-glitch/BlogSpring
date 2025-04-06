package com.example.blog.mariadb.registerTable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterTableService {

    @Autowired
    private RegisterTableRepository registerTableRepository;

    public RegisterTable findByRegisterToken(String registerToken) {
        return registerTableRepository.findByRegisterToken(registerToken);
    }


}
