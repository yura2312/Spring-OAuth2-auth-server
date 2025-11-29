package com.ecommerce.auth_server.dbinit;

import com.ecommerce.auth_server.entity.RoleEntity;
import com.ecommerce.auth_server.entity.UserEntity;
import com.ecommerce.auth_server.repository.RoleRepository;
import com.ecommerce.auth_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private RegisteredClientRepository registeredClientRepository;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public DatabaseInitializer(RegisteredClientRepository repository, PasswordEncoder passwordEncoder,
                               UserRepository userRepository, RoleRepository roleRepository) {
        this.registeredClientRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Value("${secret}")
    private String secret;

    @Override
    public void run(String... args) throws Exception {
        if (registeredClientRepository.findByClientId("client") != null) {
            return;
        }
        RegisteredClient registeredClient =
                RegisteredClient
                        .withId(UUID.randomUUID().toString())
                        .clientId("client")
                        .clientSecret(passwordEncoder.encode(secret))
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .redirectUri("http://127.0.0.1:8080/login/oauth2/code/client")
                        .scope(OidcScopes.OPENID)
                        .scope(OidcScopes.PROFILE)
                        .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                        .build();
        registeredClientRepository.save(registeredClient);

        var adminRoleEntity = roleRepository.findByRoleType(RoleEntity.RoleType.ROLE_ADMIN).orElseGet(
                () -> roleRepository.save(RoleEntity.builder().roleType(RoleEntity.RoleType.ROLE_ADMIN).build()));

        var admin = userRepository.findByUsername("admin");
        admin.ifPresentOrElse(
                (a) -> {
                },
                () -> {
                    var user = UserEntity.builder()
                            .username("admin")
                            .password(passwordEncoder.encode("123"))
                            .role(Set.of(adminRoleEntity))
                            .build();
                    userRepository.save(user);
                }
        );
    }

}
