package com.code81.library.lms.service;

import com.code81.library.lms.entity.Category;
import com.code81.library.lms.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }


    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }


    public List<Category> findRootCategories() {

        return categoryRepository.findAll().stream()
                .filter(category -> category.getParentCategory() == null)
                .toList();
    }

    @Transactional
    public Category saveCategory(Category category) {

        if (category.getParentCategory() != null && category.getParentCategory().getId() != null) {
            categoryRepository.findById(category.getParentCategory().getId())
                    .ifPresent(category::setParentCategory);
        } else {
            category.setParentCategory(null);
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Category categoryDetails) {
        return categoryRepository.findById(categoryDetails.getId())
                .map(existingCategory -> {
                    existingCategory.setName(categoryDetails.getName());

                    if (categoryDetails.getParentCategory() != null && categoryDetails.getParentCategory().getId() != null) {
                        categoryRepository.findById(categoryDetails.getParentCategory().getId())
                                .ifPresent(existingCategory::setParentCategory);
                    } else {
                        existingCategory.setParentCategory(null);
                    }
                    return categoryRepository.save(existingCategory);
                })
                .orElse(null);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}