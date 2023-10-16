package com.devdensan.mercadona.service;

import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryById(int categoryId) {
        return categoryRepository.getReferenceById(categoryId);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public boolean existsByCategoryId(int categoryId) {
        return categoryRepository.existsByCategoryId(categoryId);
    }
}
