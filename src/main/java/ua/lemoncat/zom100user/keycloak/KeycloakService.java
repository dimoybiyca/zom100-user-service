package ua.lemoncat.zom100user.keycloak;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ua.lemoncat.zom100user.DTO.*;
import ua.lemoncat.zom100user.exception.exceptions.KeycloakException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;

    public void signUp(SignUpRequest request) {
        UsersResource usersResource = this.getUsersResource();
        UserRepresentation user = createUser(request);

        try(Response response = usersResource.create(user)) {
            if(response.getStatus() != HttpStatus.CREATED.value()) {
                InputStream inputStream = (InputStream) response.getEntity();
                String errorMessage = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                throw new KeycloakException(errorMessage, HttpStatus.valueOf(response.getStatus()));
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(String userId, UpdateUserRequest request) {
        UserResource userResource = this.getUserResource(userId);

        UserRepresentation user = userResource.toRepresentation();
        user.setUsername(request.username());
        user.setAttributes(createUserAttributes(request.avatar(), request.isPublic()));
        userResource.update(user);
    }

    public void logout(String userId) {
        this.getUserResource(userId).logout();
    }

    public CheckResponse checkUsername(String username) {
        return new CheckResponse(!this.getUsersResource()
                        .searchByUsername(username, true)
                        .isEmpty()
        );
    }

    public CheckResponse checkUsername(String username, String userId) {
        return new CheckResponse(
                this.getUsersResource()
                        .searchByUsername(username, true)
                        .stream().anyMatch(user -> !Objects.equals(user.getId(), userId))
        );
    }

    public CheckResponse checkEmail(String email) {
        return new CheckResponse(!this.getUsersResource()
                .searchByEmail(email, true)
                .isEmpty()
        );
    }

    private UsersResource getUsersResource() {
        return keycloak.realm("ZOM100").users();
    }

    private UserResource getUserResource(String userId) {
        return getUsersResource().get(userId);
    }

    private UserRepresentation createUser(SignUpRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setCredentials(Collections.singletonList(createPasswordCredential(request.password())));
        user.setEnabled(true);
        String randomAvatar = String.format("user%d.webp",Math.round(Math.random() * 4));
        user.setAttributes(createUserAttributes(randomAvatar, false));
        return user;
    }

    private CredentialRepresentation createPasswordCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        return credential;
    }

    private Map<String, List<String>> createUserAttributes(String avatar, Boolean isPublic) {
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("avatar", List.of(avatar));
        attributes.put("isPublic", List.of(isPublic.toString()));

        return  attributes;
    }
}

