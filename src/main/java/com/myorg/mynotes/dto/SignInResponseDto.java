package com.myorg.mynotes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignInResponseDto {

    @JsonProperty("response-code")
    private String responseCode;
    @JsonProperty("message")
    private String message;
}
