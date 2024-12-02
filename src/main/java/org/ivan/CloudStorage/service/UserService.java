package org.ivan.CloudStorage.service;

import org.ivan.CloudStorage.exception.UserNotFoundException;
import org.ivan.CloudStorage.model.User;
import org.ivan.CloudStorage.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public User findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public boolean checkPassword(User user, String password) {
        logger.info("Checking password for user: {}", user.getUsername());
        return passwordEncoder.matches(password, user.getPassword());
    }

    public String generateToken(User user) {
        logger.info("Generating token for user: {}", user.getUsername());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public User getUserFromToken(String token) {
        logger.info("Getting user from token");
        String username = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        return findByUsername(username);
    }
}