package com.example.blog.mariadb.registerTable;

import com.example.blog.mariadb.tempUsers.TempUser;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "register_table")  // Replace with the actual table name in your database
public class RegisterTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for registerId
    @Column(name = "registerId")
    private int registerId;

    @ManyToOne
    @JoinColumn(name = "tempUserId", nullable = false) // Assume a relationship with a user
    private TempUser tempUser;

    @Column(name = "registerToken", nullable = false)
    private String registerToken;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    // Getters and setters
    public int getRegisterId() {
        return registerId;
    }

    public void setRegisterId(int registerId) {
        this.registerId = registerId;
    }

    public TempUser getTempUser() {
        return tempUser;
    }

    public void setTempUser(TempUser tempUser) {
        this.tempUser = tempUser;
    }

    public String getRegisterToken() {
        return registerToken;
    }

    public void setRegisterToken(String registerToken) {
        this.registerToken = registerToken;
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