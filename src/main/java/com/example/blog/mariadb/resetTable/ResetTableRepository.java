package com.example.blog.mariadb.resetTable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetTableRepository extends JpaRepository<ResetTable, Long> {
    Optional<ResetTable> findByResetToken(String resetToken);
}
