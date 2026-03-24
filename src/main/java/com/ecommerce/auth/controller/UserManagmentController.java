package com.ecommerce.auth.controller;

import com.ecommerce.auth.service.UserManagmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.ecommerce.auth.entity.User;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/admin")
public class UserManagmentController {
    private final UserManagmentService userManagmentService;
    public UserManagmentController(UserManagmentService userManagmentService) {
        this.userManagmentService = userManagmentService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userManagmentService.getAllUsers());
    }
    @GetMapping("/users/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        if(email == null || email.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userManagmentService.getUserByEmail(email));
    }

}
