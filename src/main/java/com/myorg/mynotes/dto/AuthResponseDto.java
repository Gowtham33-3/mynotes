package com.myorg.mynotes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDto {
    private final String accessToken;
    private final String refreshToken;
}
