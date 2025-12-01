package com.example.MyFirstApp.dtos;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String fullName;
    private String email;
    private String password;
    private String role;
}
