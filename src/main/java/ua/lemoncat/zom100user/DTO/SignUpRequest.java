package ua.lemoncat.zom100user.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
        @NotBlank(message = "username-cant-be-blank")
        String username,

        @Email(message = "email-should-be-valid")
        String email,

        @NotBlank(message = "password-cant-be-blank")
        String password) {
}
