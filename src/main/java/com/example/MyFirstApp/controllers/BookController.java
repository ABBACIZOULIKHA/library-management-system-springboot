package com.example.MyFirstApp.controllers;


import com.example.MyFirstApp.dtos.BookDto;
import com.example.MyFirstApp.entities.Author;
import com.example.MyFirstApp.entities.Book;
import com.example.MyFirstApp.entities.Category;
import com.example.MyFirstApp.mappers.BookMapper;
import com.example.MyFirstApp.repositories.AuthorRepository;
import com.example.MyFirstApp.repositories.BookRepository;
import com.example.MyFirstApp.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    @GetMapping
    public List<BookDto> getAll() {
        return bookRepository.findAllWithCategoryAndAuthors()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<BookDto> create(@RequestBody Book book, UriComponentsBuilder builder) {

        // attach category
        if (book.getCategory() != null)
            book.setCategory(categoryRepository.findById(book.getCategory().getId()).orElse(null));

        // attach authors
        if (book.getAuthors() != null) {
            List<Author> authors = authorRepository.findAllById(
                    book.getAuthors().stream().map(Author::getId).toList()
            );
            book.setAuthors(authors);
        }

        bookRepository.save(book);

        var dto = bookMapper.toDto(book);
        var uri = builder.path("/books/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        var book = bookRepository.findById(id).orElse(null);
        if (book == null)
            return ResponseEntity.notFound().build();

        bookRepository.delete(book);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{id}")
    public List<BookDto> byCategory(@PathVariable Long id) {
        return bookRepository.findByCategoryId(id)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @GetMapping("/search")
    public List<BookDto> search(@RequestParam String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

//    CONTROLLER — ADD UPLOAD ENDPOINT
    @PostMapping("/{id}/pdf")
    public ResponseEntity<?> uploadPdf(@PathVariable Long id,
                                       @RequestParam MultipartFile file) throws Exception {

        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) return ResponseEntity.notFound().build();

        if (!file.getContentType().equals("application/pdf"))
            return ResponseEntity.badRequest().body("Only PDF allowed");

        // directory
        String uploadDir = "uploads/pdfs/";
        Files.createDirectories(Paths.get(uploadDir));

        String filename = id + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + filename);

        Files.write(path, file.getBytes());

        book.setPdfPath(path.toString());
        bookRepository.save(book);

        return ResponseEntity.ok("PDF uploaded successfully");
    }

//    ADD DOWNLOAD ENDPOINT

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getPdf(@PathVariable Long id) throws Exception {

        Book book = bookRepository.findById(id).orElse(null);
        if (book == null || book.getPdfPath() == null)
            return ResponseEntity.notFound().build();

        Path path = Paths.get(book.getPdfPath());

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=book.pdf")
                .body(Files.readAllBytes(path));
    }





}
