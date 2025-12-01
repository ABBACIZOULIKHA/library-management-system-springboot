package com.example.MyFirstApp.controllers;


import com.example.MyFirstApp.dtos.CategoryDto;
import com.example.MyFirstApp.entities.Category;
import com.example.MyFirstApp.mappers.CategoryMapper;
import com.example.MyFirstApp.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody Category category, UriComponentsBuilder builder) {
        categoryRepository.save(category);
        var dto = categoryMapper.toDto(category);
        var uri = builder.path("/categories/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody Category updated) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null)
            return ResponseEntity.notFound().build();

        category.setName(updated.getName());
        category.setDescription(updated.getDescription());
        categoryRepository.save(category);

        return ResponseEntity.ok(categoryMapper.toDto(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        var category = categoryRepository.findById(id).orElse(null);
        if (category == null)
            return ResponseEntity.notFound().build();

        categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }
}
