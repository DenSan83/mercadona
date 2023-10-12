package com.devdensan.mercadona.controller;

import com.devdensan.mercadona.model.Product;
import com.devdensan.mercadona.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> home(@RequestParam(name = "category", required = false) String category) {
        if (category == null || category.isEmpty()) {
            return productService.getAllProducts();
        } else {
            return productService.getProductsByCategory(category);
        }
    }
}
