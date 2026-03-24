package com.ecommerce.auth.controller;

import com.ecommerce.auth.dto.UserManagementResponse;
import com.ecommerce.auth.entity.User;
import com.ecommerce.auth.service.UserManagmentService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@Validated
@RequestMapping("/admin")
public class UserManagmentController {
    private final UserManagmentService userManagmentService;

    public UserManagmentController(UserManagmentService userManagmentService) {
        this.userManagmentService = userManagmentService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserManagementResponse>> getAllUsers() {
        List<UserManagementResponse> users = userManagmentService.getAllUsers()
                .stream()
                .map(UserManagementResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<UserManagementResponse> getUserByEmail(
            @PathVariable @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {
        User user = userManagmentService.getUserByEmail(email);
        return ResponseEntity.ok(UserManagementResponse.from(user));
    }

    @DeleteMapping("/users/{email}")
    public ResponseEntity<Void> deleteUserByEmail(
            @PathVariable @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {
        userManagmentService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
