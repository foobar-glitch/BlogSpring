package com.example.blog.mariadb.tempUsers;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TempUserRepository extends JpaRepository<TempUser, Long> {
    TempUser findByUsername(String username);

    TempUser findByEmail(String email);
}
