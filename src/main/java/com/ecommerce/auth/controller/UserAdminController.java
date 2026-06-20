package com.ecommerce.auth.controller;

import com.ecommerce.auth.dto.UserAdminResponse;
import com.ecommerce.auth.entity.User;
import com.ecommerce.auth.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller pour les opérations d'administration des utilisateurs.
 * Endpoints réservés aux administrateurs pour gérer les utilisateurs.
 */
@RestController
@Validated
@RequestMapping("/api/v1/admin")
public class UserAdminController {
    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Récupère la liste de tous les utilisateurs actifs (admin only).
     * @return liste des utilisateurs actifs
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserAdminResponse>> getAllUsers() {
        List<UserAdminResponse> users = userService.getAllUsers()
                .stream()
                .map(UserAdminResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /**
     * Récupère un utilisateur par email (admin only).
     * @param email l'email de l'utilisateur
     * @return les détails de l'utilisateur
     */
    @GetMapping("/users/{email}")
    public ResponseEntity<UserAdminResponse> getUserByEmail(
            @PathVariable @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {
        User user = userService.getUserProfile(email);
        return ResponseEntity.ok(UserAdminResponse.from(user));
    }

    /**
     * Change le rôle d'un utilisateur (admin only).
     * @param email l'email de l'utilisateur
     * @param role le nouveau rôle (USER ou ADMIN)
     * @return l'utilisateur avec le rôle mis à jour
     */
    @PatchMapping("/users/{email}/role")
    public ResponseEntity<UserAdminResponse> updateUserRole(
            @PathVariable @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
            @RequestParam User.Role role) {
        User updatedUser = userService.updateUserRole(email, role);
        return ResponseEntity.ok(UserAdminResponse.from(updatedUser));
    }

    /**
     * Bascule l'état actif/inactif d'un utilisateur (admin only).
     * @param email l'email de l'utilisateur
     * @return l'utilisateur avec le nouvel état
     */
    @PatchMapping("/users/{email}/status")
    public ResponseEntity<UserAdminResponse> toggleUserActiveStatus(
            @PathVariable @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {
        User updatedUser = userService.toggleUserActiveStatus(email);
        return ResponseEntity.ok(UserAdminResponse.from(updatedUser));
    }

    /**
     * Supprime (soft delete) un utilisateur (admin only).
     * @param email l'email de l'utilisateur
     * @return 204 No Content si succès
     */
    @DeleteMapping("/users/{email}")
    public ResponseEntity<Void> deleteUserByEmail(
            @PathVariable @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
