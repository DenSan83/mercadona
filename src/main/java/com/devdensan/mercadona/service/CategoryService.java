package com.devdensan.mercadona.service;

import com.devdensan.mercadona.auth.RegisterRequest;
import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.repository.CategoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    public Category getCategoryById(int categoryId) {
        return categoryRepository.getReferenceById(categoryId);
    }

    public List<Category> getAllCategoriesById() {
        return categoryRepository.findAllOrderedByCategoryId();
    }

    public boolean existsByCategoryId(int categoryId) {
        return categoryRepository.existsByCategoryId(categoryId);
    }

    public Map<Integer, Integer> articlesByCategoryId() {
        List<Category> categories = categoryRepository.findAll();
        Map<Integer, Integer> articlesCountByCategory = new HashMap<>();

        for (Category category : categories) {
            int articleCount = productService.getProductsByCategoryId(category.getCategoryId()).size();
            articlesCountByCategory.put(category.getCategoryId(), articleCount);
        }

        return articlesCountByCategory;
    }

    public Category newCategory(HttpServletRequest request) {
        String categoryName = request.getParameter("category-name");
        var newCategory = new Category(categoryName);

        return categoryRepository.save(newCategory);
    }

    public Category editCategory(int categoryId, String newName) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return null;
        }
        if (categoryRepository.existsByName(newName)) {
            return null;
        }

        category.setCategoryName(newName);
        categoryRepository.save(category);
        return category;
    }

    public boolean deleteCategory(int categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int countCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.size();
    }

    public int countEmptyCategories() {
        List<Category> categories = categoryRepository.findAll();
        Map<Integer, Integer> articlesCountByCategory = new HashMap<>();
        Map<Integer, Integer> articlesByCategory = articlesByCategoryId();
        int emptyCategoriesCount = 0;

        for (Category category : categories) {
            int articleCount = articlesByCategory.getOrDefault(category.getCategoryId(), 0);
            if (articleCount == 0) {
                emptyCategoriesCount++;
            }
        }

        return emptyCategoriesCount;
    }
}
