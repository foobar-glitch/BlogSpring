package com.example.blog.mariadb.cookieTable;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cookie_table")  // Replace with your actual table name
public class CookieTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto_increment
    @Column(name = "cookie_id")
    private Long cookieId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    //Hex string
    @Column(name = "cookie_data", nullable = false)
    private String cookieData;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    // Getters and Setters
    public Long getCookieId() {
        return cookieId;
    }

    public void setCookieId(Long cookieId) {
        this.cookieId = cookieId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCookieData() {
        return cookieData;
    }

    public void setCookieData(String cookieData) {
        this.cookieData = cookieData;
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