package ua.lemoncat.zom100user.keycloak;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ua.lemoncat.zom100user.DTO.*;

@RestController
@RequestMapping(path = "zom100/user")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class KeycloakController {
    private final KeycloakService keycloakService;

    @GetMapping("check-username")
    @ResponseStatus(HttpStatus.OK)
    public CheckResponse checkUsername(@RequestParam String username,  @AuthenticationPrincipal Jwt principal) {
        return principal == null
                ? keycloakService.checkUsername(username)
                : keycloakService.checkUsername(username, principal.getSubject());
    }

    @GetMapping("check-email")
    @ResponseStatus(HttpStatus.OK)
    public CheckResponse checkEmail(@RequestParam
                                        @NotBlank(message = "email-cant-be-blank")
                                        @Email(message = "email-should-be-valid")
                                        String email) {
        return keycloakService.checkEmail(email);
    }

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid SignUpRequest request) {
        keycloakService.signUp(request);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    public void editProfile(
            @AuthenticationPrincipal Jwt principal,
            @RequestBody UpdateUserRequest request){
        keycloakService.updateUser(principal.getSubject(), request);
    }
}
