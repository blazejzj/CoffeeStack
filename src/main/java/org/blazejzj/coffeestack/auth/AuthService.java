package org.blazejzj.coffeestack.auth;

import org.blazejzj.coffeestack.auth.dto.UserLoginRequest;
import org.blazejzj.coffeestack.auth.dto.UserLoginResult;
import org.blazejzj.coffeestack.auth.dto.UserRegisterRequest;
import org.blazejzj.coffeestack.exception.EmailAlreadyExistsException;
import org.blazejzj.coffeestack.exception.InvalidCredentialsException;
import org.blazejzj.coffeestack.exception.UsernameAlreadyExistsException;
import org.blazejzj.coffeestack.security.JWTService;
import org.blazejzj.coffeestack.user.UserRepository;
import org.blazejzj.coffeestack.user.dto.UserResponse;
import org.blazejzj.coffeestack.user.models.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse registerUser(UserRegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) throw new EmailAlreadyExistsException(req.email());
        if (userRepository.existsByUsername(req.username())) throw new UsernameAlreadyExistsException(req.username());

        // create an Id for the user
        UUID id = UUID.randomUUID();
        String hashedPassword = passwordEncoder.encode(req.password());

        User user = new User(id, req.username(), req.email(), hashedPassword, LocalDateTime.now(), LocalDateTime.now() );

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getCreatedAt(), savedUser.getUpdatedAt());
    }

    public UserLoginResult loginUser(UserLoginRequest req) {

        // check first if user exists
        User user = (
                req.usernameOrEmail().contains("@")
                        ? userRepository.findByEmail(req.usernameOrEmail())
                        : userRepository.findByUsername(req.usernameOrEmail())
        ).orElseThrow(InvalidCredentialsException::new);

        // Generate JWT for the authenticated user.
        String token = jwtService.generateToken(user.getId());

        // Return both the authenticated user and the generated token.
        return new UserLoginResult(user, token);
    }
}
