package com.devdensan.mercadona.controller;

import com.devdensan.mercadona.auth.AuthenticationService;
import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.model.Product;
import com.devdensan.mercadona.model.User;
import com.devdensan.mercadona.service.CategoryService;
import com.devdensan.mercadona.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    private ProductService productService;
    private CategoryService categoryService;
    private AuthenticationService authenticationService;

    @Autowired
    public void ProductController(ProductService productService, CategoryService categoryService, AuthenticationService authenticationService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.authenticationService = authenticationService;
    }

    public HomeController(ProductService productService, CategoryService categoryService, AuthenticationService authenticationService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String home(@RequestParam(name = "category", required = false) Integer categoryId, Model model) {
        if (categoryId == null) {
            model.addAttribute("category", null);
            model.addAttribute("products", productService.getAllProducts());
        } else {
            Category category = categoryService.getCategoryById(categoryId);
            model.addAttribute("category", category);
            model.addAttribute("products", productService.getProductsByCategoryId(categoryId));
        }

        // Category List
        List<Category> categories = categoryService.getAllCategoriesById();
        model.addAttribute("categories", categories);

        // Check if any user is connected
        User connectedUser = authenticationService.getAuthenticatedUser();
        if (connectedUser == null) {
            model.addAttribute("userLoggedIn", false);
        } else {
            model.addAttribute("userLoggedIn", true);
            model.addAttribute("connectedUser", connectedUser);
        }
        return "front/home";
    }

    @GetMapping("/ajax-products")
    public String loadProductsByCategory(@RequestParam(name = "category", required = false) Integer categoryId, Model model) {
        List<Product> products;
        Category category;

        if (categoryId == null) {
            products = productService.getAllProducts();
            category = null;
        } else {
            if (!categoryService.existsByCategoryId(categoryId)) {
                model.addAttribute("products", null);
                model.addAttribute("category", null);
                return "front/components/category-error";
            }
            products = productService.getProductsByCategoryId(categoryId);
            category = categoryService.getCategoryById(categoryId);
        }

        model.addAttribute("products", products);
        model.addAttribute("category", category);

        return "front/components/products";
    }

}
