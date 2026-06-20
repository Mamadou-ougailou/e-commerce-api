package com.ecommerce.auth.service;
import com.ecommerce.auth.dto.ChangePasswordRequest;
import com.ecommerce.auth.dto.UpdateProfileRequest;
import com.ecommerce.auth.entity.User;
import com.ecommerce.auth.repository.UserRepository;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.exception.InvalidPasswordException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Service pour gérer les utilisateurs et leurs profils.
 * Fournit les opérations pour les utilisateurs (profil) et les administrateurs (gestion).
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Récupère le profil d'un utilisateur par email.
     * @param email l'email de l'utilisateur
     * @return l'utilisateur trouvé
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas
     */
    public User getUserProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Change le password d'un utilisateur et enregistre la date du changement.
     * @param email l'email de l'utilisateur
     * @param request contenant le password actuel et le nouveau password
     * @throws InvalidPasswordException si le password actuel est incorrect
     */
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request){
        User user = getUserProfile(email);

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setPasswordChangedAt(Instant.now());
        userRepository.save(user);
    }

    /**
     * Met à jour le profil utilisateur avec les données fournis.
     * Seuls les champs non-null sont mis à jour.
     * @param email l'email de l'utilisateur
     * @param request contenant les nouvelles données de profil
     * @return l'utilisateur mis à jour
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas
     */
    @Transactional
    public User updateUserProfile(String email, UpdateProfileRequest request) {
        User user = getUserProfile(email);

        if (request.firstName() != null)   user.setFirstName(request.firstName());
        if (request.lastName() != null)   user.setLastName(request.lastName());
        if (request.phoneNumber() != null)   user.setPhoneNumber(request.phoneNumber());
        if (request.country() != null)   user.setCountry(request.country());
        if (request.city() != null)   user.setCity(request.city());
        if (request.address() != null)   user.setAddress(request.address());
        if (request.zipCode() != null)   user.setZipCode(request.zipCode());

        return userRepository.save(user);
    }

    /**
     * Supprime (soft delete) un compte utilisateur en le marquant comme inactif.
     * @param email l'email de l'utilisateur à supprimer
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas
     */
    @Transactional
    public void deleteUserByEmail(String email) {
        User user = getUserProfile(email);
        user.setActive(false);
        userRepository.save(user);
    }

    // ── Admin Operations ──

    /**
     * Récupère un utilisateur par son numéro de téléphone.
     * @param phoneNumber le numéro de téléphone
     * @return l'utilisateur trouvé
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas
     */
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Récupère la liste de tous les utilisateurs actifs.
     * @return liste des utilisateurs avec isActive = true
     */
    public List<User> getAllUsers() {
        return userRepository.findByIsActiveTrue();
    }

    /**
     * Met à jour le rôle d'un utilisateur (admin only).
     * @param email l'email de l'utilisateur
     * @param role le nouveau rôle (USER ou ADMIN)
     * @return l'utilisateur avec le rôle mis à jour
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas
     */
    @Transactional
    public User updateUserRole(String email, User.Role role) {
        User user = getUserProfile(email);
        user.setRole(role);
        return userRepository.save(user);
    }

    /**
     * Bascule l'état actif d'un utilisateur (admin only).
     * Marque comme inactif si actif, actif si inactif.
     * @param email l'email de l'utilisateur
     * @return l'utilisateur avec le nouvel état
     * @throws ResourceNotFoundException si l'utilisateur n'existe pas
     */
    @Transactional
    public User toggleUserActiveStatus(String email) {
        User user = getUserProfile(email);
        user.setActive(!user.isActive());
        return userRepository.save(user);
    }

    /**
     * Vérifie si un email existe dans la base de données.
     * @param email l'email à vérifier
     * @return true si l'email existe, false sinon
     */
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Vérifie si un numéro de téléphone existe dans la base de données.
     * @param phoneNumber le numéro à vérifier
     * @return true si le numéro existe, false sinon
     */
    public boolean isPhoneNumberExists(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
