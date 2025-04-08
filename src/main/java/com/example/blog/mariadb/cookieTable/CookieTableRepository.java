package com.example.blog.mariadb.cookieTable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CookieTableRepository extends JpaRepository<CookieTable, Long> {
    Optional<CookieTable> findByCookieData(String cookieData);
}
