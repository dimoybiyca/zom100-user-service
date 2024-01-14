package ua.lemoncat.zom100user.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KeycloakProvider {

    @Value("${keycloak.url}")
    private String url;

    @Value(("${keycloak.realm}"))
    private String realm;

    @Value(("${keycloak.client.id}"))
    private String clientId;

    @Value(("${keycloak.client.secret}"))
    private String clientSecret;

    @Value(("${keycloak.admin.name}"))
    private String userName;

    @Value(("${keycloak.admin.password}"))
    private String password;

    @Bean
    Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(url)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.PASSWORD)
                .username(userName)
                .password(password)
                .build();
    }
}
