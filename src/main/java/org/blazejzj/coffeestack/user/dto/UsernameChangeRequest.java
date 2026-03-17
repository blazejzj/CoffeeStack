package org.blazejzj.coffeestack.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UsernameChangeRequest(
        @NotNull @NotBlank @Length(min = 2, max = 50)
        String newUsername
) {
}
