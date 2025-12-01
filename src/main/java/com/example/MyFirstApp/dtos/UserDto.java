package com.example.MyFirstApp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {

    @JsonProperty("user_id")
    private Long id;

    private String fullName;
    private String email;
    private String role;
    private String status;
}
