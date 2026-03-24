package org.blazejzj.coffeestack.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmailChangeRequest(
        @Email @NotBlank @NotNull
        String newEmail
) {
}
