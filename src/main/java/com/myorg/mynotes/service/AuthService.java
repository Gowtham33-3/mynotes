package com.myorg.mynotes.service;

import com.myorg.mynotes.dto.LoginRequest;
import com.myorg.mynotes.dto.LoginResponse;
import com.myorg.mynotes.entity.RefreshToken;
import com.myorg.mynotes.entity.User;
import com.myorg.mynotes.repository.RefreshTokenRepository;
import com.myorg.mynotes.repository.UserRepository;
import com.myorg.mynotes.utils.RefreshTokenHasher;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUserName(),
                                request.getPassword()
                        )
                );

        User user = (User) authentication.getPrincipal();

        refreshTokenRepository.deleteByUser_Id(user.getId());

        String refreshToken = generateRefreshToken();
        String refreshTokenHash = RefreshTokenHasher.hash(refreshToken);

            RefreshToken entity = RefreshToken.builder()
                    .tokenHash(refreshTokenHash)
                    .user(user)
                    .expiryAt(Instant.now().plus(7, ChronoUnit.DAYS))
                    .build();
            refreshTokenRepository.save(entity);


        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        String accessToken = jwtService.generateAccessToken(user);

        return new LoginResponse(accessToken);
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByUser_Id(userId);
    }


    @Transactional
    public LoginResponse refreshAccessToken(String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh token missing");
        }

        String hashedToken = RefreshTokenHasher.hash(refreshToken);

        RefreshToken tokenEntity = refreshTokenRepository
                .findByTokenHash(hashedToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (tokenEntity.getExpiryAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(tokenEntity);
            throw new RuntimeException("Refresh token expired");
        }

        User user = tokenEntity.getUser();

        String newAccessToken = jwtService.generateAccessToken(user);

        return new LoginResponse(newAccessToken);
    }


    private String generateRefreshToken() {
        byte[] bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }




}