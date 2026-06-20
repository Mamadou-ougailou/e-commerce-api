package com.ecommerce.auth.repository;

import com.ecommerce.auth.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    List<User> findByRole(User.Role role);
    List<User> findByIsActiveTrue();

    List<User> findByIsEmailVerifiedFalse();

    long countByRole(User.Role role);
    long countByIsActiveTrue();

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
