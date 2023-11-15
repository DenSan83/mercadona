package com.devdensan.mercadona.service;

import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.model.Product;
import com.devdensan.mercadona.repository.CategoryRepository;
import com.devdensan.mercadona.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    public List<Product> getAllProductsById() {
        return productRepository.findAllOrderedByProductId();
    }

    public List<Product> getProductsByCategoryId(int categoryId) {
        boolean categoryExists = this.categoryRepository.existsByCategoryId(categoryId);

        if (categoryExists) {
            Optional<Category> category = this.categoryRepository.findById(categoryId);
            return productRepository.findByCategory(category);
        }
        return new ArrayList<>();
    }

    public Product newProduct(HttpServletRequest request) {
        int categoryId = Integer.parseInt(request.getParameter("category"));
        Optional<Category> optionalCategory  = this.categoryRepository.findById(categoryId);
        Category cat = optionalCategory.orElseThrow(() -> new RuntimeException("Erreur : catégorie non trouvée"));

        String productName = request.getParameter("product-name");
        String description = request.getParameter("description");
        float price = Float.parseFloat(request.getParameter("price"));

        Product p = new Product(productName, description, "", price, cat, null);

        return productRepository.save(p);
    }

    public Product getProductById(int productId) {
        return productRepository.getReferenceById(productId);
    }

    public boolean existsByProductId(int categoryId) {
        return productRepository.existsByProductId(categoryId);
    }

    public Product editProduct(int productId, String newName, String description, Category category, float price) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setProductName(newName);
            product.setDescription(description);
            product.setCategory(category);
            product.setPrice(price);

            productRepository.save(product);
        }
        return product;
    }

    public boolean deleteProduct(int productId) {
        try {
            productRepository.deleteById(productId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int countProducts() {
        List<Product> productList = productRepository.findAll();
        return productList.size();
    }
}
