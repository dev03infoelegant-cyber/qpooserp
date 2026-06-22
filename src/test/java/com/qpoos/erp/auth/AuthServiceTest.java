package com.qpoos.erp.auth;

import com.qpoos.erp.config.AuthProperties;
import com.qpoos.erp.dto.auth.AuthResponse;
import com.qpoos.erp.dto.auth.ForgotPasswordConfirmRequest;
import com.qpoos.erp.dto.auth.ForgotPasswordRequest;
import com.qpoos.erp.dto.auth.LoginRequest;
import com.qpoos.erp.dto.auth.RegisterRequest;
import com.qpoos.erp.dto.auth.VerifyEmailRequest;
import com.qpoos.erp.entity.EmailVerificationTokenEntity;
import com.qpoos.erp.entity.ForgotPasswordTokenEntity;
import com.qpoos.erp.entity.RefreshTokenEntity;
import com.qpoos.erp.entity.UserEntity;
import com.qpoos.erp.repository.EmailVerificationTokenRepository;
import com.qpoos.erp.repository.ForgotPasswordTokenRepository;
import com.qpoos.erp.repository.RefreshTokenRepository;
import com.qpoos.erp.repository.UserRepository;
import com.qpoos.erp.security.JwtService;
import com.qpoos.erp.security.RandomTokenService;
import com.qpoos.erp.security.TokenHashService;
import com.qpoos.erp.service.AuthService;
import com.qpoos.erp.service.DevEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private final Clock clock = Clock.fixed(Instant.parse("2026-06-21T10:00:00Z"), ZoneOffset.UTC);
    private final AuthProperties properties = new AuthProperties();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TokenHashService tokenHashService = new TokenHashService();
    private final List<UserEntity> users = new ArrayList<>();
    private final List<RefreshTokenEntity> refreshTokens = new ArrayList<>();
    private final List<EmailVerificationTokenEntity> emailTokens = new ArrayList<>();
    private final List<ForgotPasswordTokenEntity> forgotTokens = new ArrayList<>();
    private AuthService authService;
    private RecordingEmailService emailService;

    @BeforeEach
    void setUp() {
        properties.setJwtSecret("dev-only-change-this-secret-dev-only-change-this-secret");
        properties.setAccessTokenMinutes(15);
        properties.setRefreshTokenDays(30);
        properties.setEmailVerificationMinutes(1440);
        properties.setForgotPasswordMinutes(15);
        properties.setRefreshCookieName("refresh_token");
        properties.setRefreshCookieSecure(false);
        properties.setFrontendUrl("http://localhost:8080");

        UserRepository userRepository = mockUserRepository();
        RefreshTokenRepository refreshTokenRepository = mockRefreshTokenRepository();
        EmailVerificationTokenRepository emailVerificationTokenRepository = mockEmailVerificationTokenRepository();
        ForgotPasswordTokenRepository forgotPasswordTokenRepository = mockForgotPasswordTokenRepository();

        emailService = new RecordingEmailService();
        authService = new AuthService(
                userRepository,
                refreshTokenRepository,
                emailVerificationTokenRepository,
                forgotPasswordTokenRepository,
                passwordEncoder,
                new JwtService(properties, clock),
                new RandomTokenService(),
                tokenHashService,
                emailService,
                properties,
                clock
        );
    }

    @Test
    void registerCreatesUnverifiedUserAndVerificationToken() {
        authService.register(new RegisterRequest("User@Example.com", "StrongPass123!"));

        UserEntity user = findUser("user@example.com").orElseThrow();
        assertThat(user.getEmail()).isEqualTo("user@example.com");
        assertThat(user.getEmailVerified()).isFalse();
        assertThat(passwordEncoder.matches("StrongPass123!", user.getPasswordHash())).isTrue();
        assertThat(emailTokens).hasSize(1);
        assertThat(emailService.verificationLinks).hasSize(1);
    }

    @Test
    void loginBeforeEmailVerificationIsRejected() {
        authService.register(new RegisterRequest("user@example.com", "StrongPass123!"));

        assertThatThrownBy(() -> authService.login(
                new LoginRequest("user@example.com", "StrongPass123!"),
                "JUnit",
                "127.0.0.1"
        )).hasMessageContaining("Email is not verified");
    }

    @Test
    void verifyEmailAllowsLoginAndCreatesRefreshToken() {
        authService.register(new RegisterRequest("user@example.com", "StrongPass123!"));
        authService.verifyEmail(new VerifyEmailRequest(emailService.verificationTokens.get(0)));

        AuthResponse response = authService.login(
                new LoginRequest("user@example.com", "StrongPass123!"),
                "JUnit",
                "127.0.0.1"
        );

        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.expiresInSeconds()).isEqualTo(900);
        assertThat(response.refreshToken()).isNotBlank();
        assertThat(refreshTokens).hasSize(1);
        assertThat(refreshTokens.get(0).getRevokedAt()).isNull();
    }

    @Test
    void refreshRotatesRefreshTokenAndRevokesPreviousToken() {
        authService.register(new RegisterRequest("user@example.com", "StrongPass123!"));
        authService.verifyEmail(new VerifyEmailRequest(emailService.verificationTokens.get(0)));
        AuthResponse first = authService.login(new LoginRequest("user@example.com", "StrongPass123!"), "JUnit", "127.0.0.1");

        AuthResponse second = authService.refresh(first.refreshToken(), "JUnit2", "127.0.0.2");

        assertThat(second.accessToken()).isNotBlank();
        assertThat(second.refreshToken()).isNotEqualTo(first.refreshToken());
        assertThat(refreshTokens).hasSize(2);
        assertThat(findRefreshToken(tokenHashService.hash(first.refreshToken())).orElseThrow().getRevokedAt()).isNotNull();
        assertThat(findRefreshToken(tokenHashService.hash(second.refreshToken())).orElseThrow().getRevokedAt()).isNull();
    }

    @Test
    void forgotPasswordUsesGenericResponseAndConfirmationRevokesSessions() {
        authService.register(new RegisterRequest("user@example.com", "StrongPass123!"));
        authService.verifyEmail(new VerifyEmailRequest(emailService.verificationTokens.get(0)));
        AuthResponse login = authService.login(new LoginRequest("user@example.com", "StrongPass123!"), "JUnit", "127.0.0.1");

        String missingMessage = authService.forgotPassword(new ForgotPasswordRequest("missing@example.com"), "127.0.0.1", "JUnit");
        String existingMessage = authService.forgotPassword(new ForgotPasswordRequest("user@example.com"), "127.0.0.1", "JUnit");
        authService.confirmForgotPassword(new ForgotPasswordConfirmRequest(emailService.forgotPasswordTokens.get(0), "NewStrongPass123!"));

        UserEntity user = findUser("user@example.com").orElseThrow();
        assertThat(existingMessage).isEqualTo(missingMessage);
        assertThat(passwordEncoder.matches("NewStrongPass123!", user.getPasswordHash())).isTrue();
        assertThat(refreshTokens).allMatch(token -> token.getRevokedAt() != null);
        assertThatThrownBy(() -> authService.refresh(login.refreshToken(), "JUnit", "127.0.0.1"))
                .hasMessageContaining("Invalid refresh token");
    }

    private UserRepository mockUserRepository() {
        UserRepository repository = mock(UserRepository.class);
        when(repository.findByEmailIgnoreCase(any())).thenAnswer(invocation -> findUser(invocation.getArgument(0)));
        when(repository.existsByEmailIgnoreCase(any())).thenAnswer(invocation -> findUser(invocation.getArgument(0)).isPresent());
        when(repository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            if (user.getId() == null) {
                user.setId(UUID.randomUUID());
                user.setCreatedAt(OffsetDateTime.now(clock));
            }
            user.setUpdatedAt(OffsetDateTime.now(clock));
            users.removeIf(existing -> existing.getId().equals(user.getId()));
            users.add(user);
            return user;
        });
        return repository;
    }

    private RefreshTokenRepository mockRefreshTokenRepository() {
        RefreshTokenRepository repository = mock(RefreshTokenRepository.class);
        when(repository.findByTokenHash(any())).thenAnswer(invocation -> findRefreshToken(invocation.getArgument(0)));
        when(repository.findAllByUserIdAndRevokedAtIsNull(any())).thenAnswer(invocation -> {
            UUID userId = invocation.getArgument(0);
            return refreshTokens.stream()
                    .filter(token -> token.getUser().getId().equals(userId))
                    .filter(token -> token.getRevokedAt() == null)
                    .toList();
        });
        when(repository.save(any(RefreshTokenEntity.class))).thenAnswer(invocation -> {
            RefreshTokenEntity token = invocation.getArgument(0);
            if (token.getId() == null) {
                token.setId(UUID.randomUUID());
                token.setCreatedAt(OffsetDateTime.now(clock));
            }
            refreshTokens.removeIf(existing -> existing.getId().equals(token.getId()));
            refreshTokens.add(token);
            return token;
        });
        when(repository.saveAll(any())).thenAnswer(invocation -> {
            Iterable<RefreshTokenEntity> tokens = invocation.getArgument(0);
            List<RefreshTokenEntity> saved = new ArrayList<>();
            tokens.forEach(token -> {
                refreshTokens.removeIf(existing -> existing.getId().equals(token.getId()));
                refreshTokens.add(token);
                saved.add(token);
            });
            return saved;
        });
        return repository;
    }

    private EmailVerificationTokenRepository mockEmailVerificationTokenRepository() {
        EmailVerificationTokenRepository repository = mock(EmailVerificationTokenRepository.class);
        when(repository.findByTokenHash(any())).thenAnswer(invocation -> findEmailToken(invocation.getArgument(0)));
        when(repository.save(any(EmailVerificationTokenEntity.class))).thenAnswer(invocation -> {
            EmailVerificationTokenEntity token = invocation.getArgument(0);
            if (token.getId() == null) {
                token.setId(UUID.randomUUID());
                token.setCreatedAt(OffsetDateTime.now(clock));
            }
            emailTokens.removeIf(existing -> existing.getId().equals(token.getId()));
            emailTokens.add(token);
            return token;
        });
        return repository;
    }

    private ForgotPasswordTokenRepository mockForgotPasswordTokenRepository() {
        ForgotPasswordTokenRepository repository = mock(ForgotPasswordTokenRepository.class);
        when(repository.findByTokenHash(any())).thenAnswer(invocation -> findForgotToken(invocation.getArgument(0)));
        when(repository.save(any(ForgotPasswordTokenEntity.class))).thenAnswer(invocation -> {
            ForgotPasswordTokenEntity token = invocation.getArgument(0);
            if (token.getId() == null) {
                token.setId(UUID.randomUUID());
                token.setCreatedAt(OffsetDateTime.now(clock));
            }
            forgotTokens.removeIf(existing -> existing.getId().equals(token.getId()));
            forgotTokens.add(token);
            return token;
        });
        return repository;
    }

    private Optional<UserEntity> findUser(String email) {
        return users.stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    private Optional<RefreshTokenEntity> findRefreshToken(String tokenHash) {
        return refreshTokens.stream().filter(token -> token.getTokenHash().equals(tokenHash)).findFirst();
    }

    private Optional<EmailVerificationTokenEntity> findEmailToken(String tokenHash) {
        return emailTokens.stream().filter(token -> token.getTokenHash().equals(tokenHash)).findFirst();
    }

    private Optional<ForgotPasswordTokenEntity> findForgotToken(String tokenHash) {
        return forgotTokens.stream().filter(token -> token.getTokenHash().equals(tokenHash)).findFirst();
    }

    private static final class RecordingEmailService extends DevEmailService {
        private final List<String> verificationLinks = new ArrayList<>();
        private final List<String> verificationTokens = new ArrayList<>();
        private final List<String> forgotPasswordTokens = new ArrayList<>();

        RecordingEmailService() {
            super("http://localhost:8080");
        }

        @Override
        public void sendVerificationLink(String email, String token) {
            verificationTokens.add(token);
            verificationLinks.add("verify:" + email + ":" + token);
        }

        @Override
        public void sendForgotPasswordLink(String email, String token) {
            forgotPasswordTokens.add(token);
        }
    }
}
