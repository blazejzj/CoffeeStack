package org.blazejzj.coffeestack.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record PasswordChangeRequest(
        @NotBlank @NotNull @Length(min = 8)
        String oldPassword,

        @NotBlank @NotNull @Length(min = 8)
        String newPassword
) {
}
