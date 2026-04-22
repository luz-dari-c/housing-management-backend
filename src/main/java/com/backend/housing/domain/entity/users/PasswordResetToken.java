package com.backend.housing.domain.entity.users;

import java.time.LocalDateTime;

public class PasswordResetToken {
    private Long id;
    private String email;
    private String code;
    private LocalDateTime expiresAt;
    private boolean used;

    public PasswordResetToken() {}

    public PasswordResetToken(String email, String code, LocalDateTime expiresAt) {
        this.email = email;
        this.code = code;
        this.expiresAt = expiresAt;
        this.used = false;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }

    public boolean isValid() {
        return !used && expiresAt.isAfter(LocalDateTime.now());
    }
}