package com.example.MyFirstApp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;

    @JsonProperty("token_type")
    private String tokenType = "Bearer";

    @JsonProperty("expires_in")
    private long expiresIn; // in milliseconds

    private UserDto user;
}