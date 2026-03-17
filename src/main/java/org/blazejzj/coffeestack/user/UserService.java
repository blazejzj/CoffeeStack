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
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        // the principal stored in the iflter is user uuid
        assert authentication != null;
        UUID userId = (UUID) authentication.getPrincipal();

        // get user from db
        assert userId != null;
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
        // See what user it is by the ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException();
        }

        UUID userId = (UUID )authentication.getPrincipal();

        // See if user exists in database (is it redundant? (JWT))
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        // check if passwords match first
        if (!passwordEncoder.matches(req.oldPassword(), user.getPasswordHash())) {
            throw new WrongPasswordException();
        }

        user.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        return new MessageResponse("Password changed successfully");
    }
}
