package com.yourapp.model.enums;

public enum UserStatus {
    PENDING,      // registered, waiting for admin approval (default in User)
    ACTIVE,       // approved (FR-02)
    REJECTED,     // denied by the admin (FR-02)
    DEACTIVATED   // disabled later by the admin
}