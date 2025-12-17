package com.myorg.mynotes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignInRequestDto {
    @JsonProperty("user-name")
     private String userName;
    @JsonProperty("password")
    private String Password;
    @JsonProperty("email")
    private String email;
    @JsonProperty("role")
    private String role;

}
