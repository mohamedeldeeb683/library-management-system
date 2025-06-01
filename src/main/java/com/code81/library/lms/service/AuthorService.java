package com.code81.library.lms.service;

import com.code81.library.lms.entity.Author;
import com.code81.library.lms.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class AuthorService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }


    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }


    public Optional<Author> findAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author updateAuthor(Author authorDetails) {
        return authorRepository.findById(authorDetails.getId())
                .map(existingAuthor -> {
                    existingAuthor.setName(authorDetails.getName());
                    existingAuthor.setBiography(authorDetails.getBiography());
                    return authorRepository.save(existingAuthor);
                })
                .orElse(null);
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}