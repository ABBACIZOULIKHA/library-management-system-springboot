package com.example.MyFirstApp.controllers;


import com.example.MyFirstApp.dtos.AuthorDto;
import com.example.MyFirstApp.entities.Author;
import com.example.MyFirstApp.mappers.AuthorMapper;
import com.example.MyFirstApp.repositories.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @GetMapping
    public List<AuthorDto> getAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getById(@PathVariable Long id) {
        var author = authorRepository.findById(id).orElse(null);
        if (author == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(authorMapper.toDto(author));
    }

    @PostMapping
    public ResponseEntity<AuthorDto> create(@RequestBody Author author, UriComponentsBuilder builder) {
        authorRepository.save(author);
        var dto = authorMapper.toDto(author);
        var uri = builder.path("/authors/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> update(@PathVariable Long id, @RequestBody Author updated) {
        var author = authorRepository.findById(id).orElse(null);
        if (author == null)
            return ResponseEntity.notFound().build();

        author.setName(updated.getName());
        author.setBio(updated.getBio());
        authorRepository.save(author);

        return ResponseEntity.ok(authorMapper.toDto(author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        var author = authorRepository.findById(id).orElse(null);
        if (author == null)
            return ResponseEntity.notFound().build();

        authorRepository.delete(author);
        return ResponseEntity.noContent().build();
    }
}
