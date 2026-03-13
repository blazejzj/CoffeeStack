package org.blazejzj.coffeestack.auth;

import org.blazejzj.coffeestack.auth.dto.UserLoginRequest;
import org.blazejzj.coffeestack.auth.dto.UserRegisterRequest;
import org.blazejzj.coffeestack.exception.EmailAlreadyExistsException;
import org.blazejzj.coffeestack.exception.InvalidCredentialsException;
import org.blazejzj.coffeestack.exception.UsernameAlreadyExistsException;
import org.blazejzj.coffeestack.user.UserRepository;
import org.blazejzj.coffeestack.user.dto.UserResponse;
import org.blazejzj.coffeestack.user.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    public UserResponse loginUser(UserLoginRequest req) {

        // check first if user exists
        User user = (
                req.usernameOrEmail().contains("@")
                        ? userRepository.findByEmail(req.usernameOrEmail())
                        : userRepository.findByUsername(req.usernameOrEmail())
        ).orElseThrow(InvalidCredentialsException::new);

        // if exist check the password
        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) throw new InvalidCredentialsException();

        // Now we generate JWT token with all the information
        // we also need to "pack" the JWT into a cookie and send it together with the response.

    }
}
