package com.ecommerce.auth.dto;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    String firstName,
    String lastName,
    String email
) {}