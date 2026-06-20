package com.ecommerce.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.auth.dto.ChangePasswordRequest;
import com.ecommerce.auth.dto.UpdateProfileRequest;
import com.ecommerce.auth.dto.UserProfileResponse;
import com.ecommerce.auth.entity.User;
import com.ecommerce.auth.service.UserService;

import jakarta.validation.Valid;

/**
 * Controller pour gérer le profil de l'utilisateur connecté.
 * Fournit les endpoints pour consulter et modifier ses propres données.
 */
@RestController
@RequestMapping("/api/v1/users/profile")
public class UserProfileController {

    private final UserService userService;
    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Récupère le profil de l'utilisateur connecté.
     * @param user l'utilisateur connecté (injecté par Spring Security)
     * @return le profil de l'utilisateur
     */
    @GetMapping
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(UserProfileResponse.from(user));
    }

    /**
     * Met à jour le profil de l'utilisateur connecté.
     * @param user l'utilisateur connecté
     * @param request contenant les données à mettre à jour
     * @return le profil mis à jour
     */
    @PatchMapping
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateProfileRequest request){
        User updatedUser = userService.updateUserProfile(user.getEmail(), request);
        return ResponseEntity.ok(UserProfileResponse.from(updatedUser));
    }

    /**
     * Change le password de l'utilisateur connecté.
     * @param user l'utilisateur connecté
     * @param request contenant l'ancien et le nouveau password
     * @return 204 No Content si succès
     */
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(user.getEmail(), request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Supprime le compte de l'utilisateur connecté (soft delete).
     * @param user l'utilisateur connecté
     * @return 204 No Content si succès
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal User user) {
        userService.deleteUserByEmail(user.getEmail());
        return ResponseEntity.noContent().build();
    }
}
