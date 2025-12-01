package com.example.MyFirstApp.controllers;



import com.example.MyFirstApp.dtos.*;
import com.example.MyFirstApp.mappers.UserMapper;
import com.example.MyFirstApp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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

        if (!userRepository.existsById(id))
            return ResponseEntity.notFound().build();

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ) {

        var user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        if (!user.getPassword().equals(request.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {

        var userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }

        var user = userOpt.get();

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }

        UserDto dto = userMapper.toDto(user);
        return ResponseEntity.ok(dto);
    }

}
