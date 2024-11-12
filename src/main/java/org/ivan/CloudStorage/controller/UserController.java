package org.ivan.CloudStorage.controller;

import org.ivan.CloudStorage.model.User;
import org.ivan.CloudStorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        User user = userService.findByUsername(credentials.get("username"));
        if (user != null && userService.checkPassword(user, credentials.get("password"))) {
            String token = userService.generateToken(user);
            return ResponseEntity.ok(Collections.singletonMap("auth-token", token));
        } else {
            return ResponseEntity.status(401).build();
        }
    }
}