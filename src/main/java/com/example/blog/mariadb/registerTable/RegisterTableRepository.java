package com.example.blog.mariadb.registerTable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterTableRepository extends JpaRepository<RegisterTable, Long> {
    // Finding by hashed token
    RegisterTable findByRegisterToken(String registerToken);

}
