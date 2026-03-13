package org.blazejzj.coffeestack.user;

import org.blazejzj.coffeestack.exception.UserNotFoundException;
import org.blazejzj.coffeestack.user.dto.UserResponse;
import org.blazejzj.coffeestack.user.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
