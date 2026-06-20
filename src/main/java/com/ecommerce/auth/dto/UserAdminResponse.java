package com.ecommerce.auth.dto;

import com.ecommerce.auth.entity.User;

import java.time.Instant;

public record UserAdminResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        User.Role role,
        Instant createdAt,
        Instant updatedAt,
        Instant lastLoginAt,
        Instant passwordChangedAt,
        boolean isActive,
        boolean isEmailVerified,
        String country,
        String city,
        String address,
        String zipCode

    ) {

    public static UserAdminResponse from(User user) {
        return new UserAdminResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLoginAt(),
                user.getPasswordChangedAt(),
                user.isActive(),
                user.isEmailVerified(),
                user.getCountry(),
                user.getCity(),
                user.getAddress(),
                user.getZipCode()
        );
    }
}