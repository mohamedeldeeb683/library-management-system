package com.code81.library.lms.repository;

import com.code81.library.lms.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}