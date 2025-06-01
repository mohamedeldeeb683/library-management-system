package com.code81.library.lms.controller;

import com.code81.library.lms.entity.Author;
import com.code81.library.lms.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.validation.Valid;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "anonymous";
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Author>> getAllAuthors() {
        logger.info("User {} is attempting to retrieve all authors.", getCurrentUsername());
        List<Author> authors = authorService.findAllAuthors();
        logger.info("User {} successfully retrieved {} authors.", getCurrentUsername(), authors.size());
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        logger.info("User {} is attempting to retrieve author with ID: {}.", getCurrentUsername(), id);
        Optional<Author> author = authorService.findAuthorById(id);
        if (author.isPresent()) {
            logger.info("User {} successfully retrieved author with ID: {}.", getCurrentUsername(), id);
            return ResponseEntity.ok(author.get());
        } else {
            logger.warn("User {} failed to retrieve author with ID: {}. Author not found.", getCurrentUsername(), id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody Author author) { // إضافة @Valid
        String username = getCurrentUsername();
        logger.info("User {} is attempting to create a new author: {}.", username, author.getName());
        Author savedAuthor = authorService.saveAuthor(author);
        logger.info("User {} successfully created author with ID: {} and name: {}.", username, savedAuthor.getId(), savedAuthor.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @Valid @RequestBody Author authorDetails) { // إضافة @Valid
        String username = getCurrentUsername();
        logger.info("User {} is attempting to update author with ID: {}.", username, id);
        authorDetails.setId(id);
        Author updatedAuthor = authorService.updateAuthor(authorDetails);
        if (updatedAuthor != null) {
            logger.info("User {} successfully updated author with ID: {}.", username, id);
            return ResponseEntity.ok(updatedAuthor);
        } else {
            logger.warn("User {} failed to update author with ID: {}. Author not found.", username, id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        String username = getCurrentUsername();
        logger.info("User {} is attempting to delete author with ID: {}.", username, id);
        authorService.deleteAuthor(id);
        logger.info("User {} successfully deleted author with ID: {}.", username, id);
        return ResponseEntity.noContent().build();
    }
}