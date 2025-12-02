package com.example.MyFirstApp.controllers;

import com.example.MyFirstApp.dtos.*;
import com.example.MyFirstApp.entities.enums.UserStatus;
import com.example.MyFirstApp.mappers.UserMapper;
import com.example.MyFirstApp.repositories.UserRepository;
import com.example.MyFirstApp.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @GetMapping
    public List<UserDto> getAllUsers(
            @RequestParam(defaultValue = "fullName") String sort
    ) {
        // Allowed sort fields mapping (API → ENTITY)
        Map<String, String> sortMapping = Map.of(
                "name", "fullName",
                "fullName", "fullName",
                "email", "email",
                "createdAt", "createdAt"
        );

        String sortBy = sortMapping.getOrDefault(sort, "fullName");

        return userRepository.findAll(Sort.by(sortBy)).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Hash the password before saving
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        var user = userMapper.toEntity(request);
        userRepository.save(user);

        var dto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        userMapper.update(request, user);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Soft delete: mark as INACTIVE instead of deleting
        user.setStatus(UserStatus.SUSPENDED);
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

    // Optional: Add a hard delete endpoint for admins only
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> permanentDelete(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .build(); // User has related records
        }
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        // Verify old password using BCrypt
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Old password is incorrect");
        }

        // Hash and save new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Get user details
            var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

            // Generate JWT token
            String jwtToken = jwtUtil.generateToken(user);

            // Build and return response
            AuthenticationResponse response = AuthenticationResponse.builder()
                    .token(jwtToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtUtil.getExpirationTime())
                    .user(userMapper.toDto(user))
                    .build();

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}