package com.example.blog.mariadb.resetTable;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reset_table",
        uniqueConstraints = @UniqueConstraint(columnNames = "reset_token"))
public class ResetTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reset_id")
    private Long resetId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "reset_token", nullable = false, length = 255)
    private String resetToken;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;


    // Getters and Setters
    public Long getResetId() {
        return resetId;
    }

    public void setResetId(Long resetId) {
        this.resetId = resetId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }
}

