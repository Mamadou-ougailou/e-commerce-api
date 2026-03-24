package com.ecommerce.auth.dto;

import com.ecommerce.auth.entity.User;

import java.util.Date;

public record UserManagementResponse(
        Long id,
        String username,
        String email,
        User.Role role,
        Date createdAt) {

    public static UserManagementResponse from(User user) {
        return new UserManagementResponse(
                user.getId(),
                user.getDisplayName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt());
    }
}