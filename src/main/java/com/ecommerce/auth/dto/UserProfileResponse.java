package com.ecommerce.auth.dto;

import com.ecommerce.auth.entity.User;

import java.time.Instant;

public record UserProfileResponse(
        String firstName,
        String lastName,
        String email,
        User.Role role,
        Instant createdAt

    ) {

    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}