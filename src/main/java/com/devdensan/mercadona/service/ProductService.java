package com.devdensan.mercadona.service;

import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.model.Product;
import com.devdensan.mercadona.repository.CategoryRepository;
import com.devdensan.mercadona.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategoryId(int categoryId) {
        boolean categoryExists = this.categoryRepository.existsByCategoryId(categoryId);

        if (categoryExists) {
            Optional<Category> category = this.categoryRepository.findById(categoryId);
            return productRepository.findByCategory(category);
        }
        return new ArrayList<>();
    }

}
