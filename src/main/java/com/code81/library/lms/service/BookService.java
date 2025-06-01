package com.code81.library.lms.service;

import com.code81.library.lms.entity.Author;
import com.code81.library.lms.entity.Book;
import com.code81.library.lms.entity.Category;
import com.code81.library.lms.entity.Publisher;
import com.code81.library.lms.repository.BookRepository;
import com.code81.library.lms.repository.AuthorRepository;
import com.code81.library.lms.repository.CategoryRepository;
import com.code81.library.lms.repository.PublisherRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository,
                       PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public Book saveBook(Book book) {

        Set<Author> authors = new HashSet<>();
        if (book.getAuthors() != null) {
            for (Author author : book.getAuthors()) {
                if (author.getId() != null) {
                    authorRepository.findById(author.getId()).ifPresent(authors::add);
                } else {

                    authors.add(authorRepository.save(author));
                }
            }
        }
        book.setAuthors(authors);


        Set<Category> categories = new HashSet<>();
        if (book.getCategories() != null) {
            for (Category category : book.getCategories()) {
                if (category.getId() != null) {
                    categoryRepository.findById(category.getId()).ifPresent(categories::add);
                } else {
                    categories.add(categoryRepository.save(category));
                }
            }
        }
        book.setCategories(categories);


        if (book.getPublisher() != null && book.getPublisher().getId() != null) {

            Publisher publisherReference = entityManager.getReference(Publisher.class, book.getPublisher().getId());
            book.setPublisher(publisherReference);
        } else {
            book.setPublisher(null);
        }

        return bookRepository.save(book);
    }


    @Transactional
    public Book updateBook(Book bookDetails) {
        return bookRepository.findById(bookDetails.getId())
                .map(existingBook -> {
                    existingBook.setTitle(bookDetails.getTitle());
                    existingBook.setIsbn(bookDetails.getIsbn());
                    existingBook.setPublicationYear(bookDetails.getPublicationYear());
                    existingBook.setEdition(bookDetails.getEdition());
                    existingBook.setSummary(bookDetails.getSummary());
                    existingBook.setCoverImageUrl(bookDetails.getCoverImageUrl());
                    existingBook.setLanguage(bookDetails.getLanguage());


                    if (bookDetails.getPublisher() != null && bookDetails.getPublisher().getId() != null) {

                        Publisher publisherReference = entityManager.getReference(Publisher.class, bookDetails.getPublisher().getId());
                        existingBook.setPublisher(publisherReference);
                    } else {
                        existingBook.setPublisher(null);
                    }


                    Set<Author> updatedAuthors = new HashSet<>();
                    if (bookDetails.getAuthors() != null) {
                        for (Author author : bookDetails.getAuthors()) {
                            if (author.getId() != null) {
                                authorRepository.findById(author.getId()).ifPresent(updatedAuthors::add);
                            } else {
                                updatedAuthors.add(authorRepository.save(author));
                            }
                        }
                    }
                    existingBook.setAuthors(updatedAuthors);


                    Set<Category> updatedCategories = new HashSet<>();
                    if (bookDetails.getCategories() != null) {
                        for (Category category : bookDetails.getCategories()) {
                            if (category.getId() != null) {
                                categoryRepository.findById(category.getId()).ifPresent(updatedCategories::add);
                            } else {
                                updatedCategories.add(categoryRepository.save(category));
                            }
                        }
                    }
                    existingBook.setCategories(updatedCategories);

                    return bookRepository.save(existingBook);
                })
                .orElse(null);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}