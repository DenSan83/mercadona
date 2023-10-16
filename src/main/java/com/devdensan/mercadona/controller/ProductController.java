package com.devdensan.mercadona.controller;

import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.model.Product;
import com.devdensan.mercadona.service.CategoryService;
import com.devdensan.mercadona.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @RequestMapping(value = { "products", "products/" })
    public List<Product> productList(@RequestParam(name = "category", required = false) Integer categoryId) {
        if (categoryId == null) {
            return productService.getAllProducts();
        } else {
            return productService.getProductsByCategoryId(categoryId);
        }
    }

    public List<Category> categoryList() {
        return categoryService.getAllCategories();
    }
}
