package org.blazejzj.coffeestack.user;

import org.blazejzj.coffeestack.exception.UserNotFoundException;
import org.blazejzj.coffeestack.user.dto.UserResponse;
import org.blazejzj.coffeestack.user.models.User;
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
}
