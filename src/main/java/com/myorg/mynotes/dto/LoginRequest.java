package com.myorg.mynotes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @JsonProperty("username")
    private String userName;
    @JsonProperty("password")
    private String password;

}
