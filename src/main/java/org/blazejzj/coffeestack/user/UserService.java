package org.blazejzj.coffeestack.user;

import jakarta.transaction.Transactional;
import org.blazejzj.coffeestack.exception.UnauthorizedException;
import org.blazejzj.coffeestack.exception.UserNotFoundException;
import org.blazejzj.coffeestack.exception.WrongPasswordException;
import org.blazejzj.coffeestack.user.dto.MessageResponse;
import org.blazejzj.coffeestack.user.dto.PasswordChangeRequest;
import org.blazejzj.coffeestack.user.dto.UserResponse;
import org.blazejzj.coffeestack.user.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
    }

    public UserResponse getMe() {
        UUID userId = getUserPrincipal();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public MessageResponse changePassword(PasswordChangeRequest req) {
        UUID userId = getUserPrincipal();

        // See if user exists in database (is it redundant? (JWT))
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        if (!passwordEncoder.matches(req.oldPassword(), user.getPasswordHash())) {
            throw new WrongPasswordException();
        }

        user.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        return new MessageResponse("Password changed successfully");
    }

    private static UUID getUserPrincipal() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException();
        }

        return (UUID) authentication.getPrincipal();
    }
}
