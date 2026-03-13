package org.blazejzj.coffeestack.user.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter @Getter
@Entity(name = "users")
public class User {
    @Id
    private UUID id;

    private String username;

    private String email;

    private String passwordHash;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public User(UUID id, String username, String email, String passwordHash, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User() {}
}
