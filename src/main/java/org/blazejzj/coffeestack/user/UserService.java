package org.blazejzj.coffeestack.user;

import org.blazejzj.coffeestack.exception.UserNotFoundException;
import org.blazejzj.coffeestack.user.dto.PasswordChangeRequest;
import org.blazejzj.coffeestack.user.dto.UserInfoChangeResponse;
import org.blazejzj.coffeestack.user.dto.UserResponse;
import org.blazejzj.coffeestack.user.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
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

    public UserInfoChangeResponse changePassword(PasswordChangeRequest req) {
        // See what user it is by the ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        UUID userId = (UUID )authentication.getPrincipal();

        // See if user exists in database (is it redundant? (JWT))
        assert userId != null;
        User savedUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        // check if passwords match first
        if (passwordEncoder.matches(req.oldPassword(), savedUser.getPasswordHash())) {
            // throew something here, password do not match
        }

        String newPasswordHash = passwordEncoder.encode(req.newPassword());

        // create new user and replace it
        User user = new User(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), newPasswordHash, savedUser.getCreatedAt(), LocalDateTime.now());

        // save user to repo
        userRepository.save(user);

        return new UserInfoChangeResponse("Password successfully updated.");
    }
}
