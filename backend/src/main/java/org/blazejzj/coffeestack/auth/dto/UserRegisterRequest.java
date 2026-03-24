package org.blazejzj.coffeestack.auth.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record UserRegisterRequest(
        @NotNull @Email @NotBlank
        String email,

        @NotNull @NotBlank @Length(min = 2, max = 50)
        String username,

        @NotNull @NotBlank @Length(min = 8)
        String password
) {
}
