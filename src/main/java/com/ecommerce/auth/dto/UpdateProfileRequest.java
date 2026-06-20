package com.ecommerce.auth.dto;

public record UpdateProfileRequest (
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        String city,
        String country,
        String zipCode
) {}