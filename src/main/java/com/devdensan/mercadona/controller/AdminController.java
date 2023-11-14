package com.devdensan.mercadona.controller;

import com.devdensan.mercadona.auth.AuthenticationService;
import com.devdensan.mercadona.model.User;
import com.devdensan.mercadona.service.CategoryService;
import com.devdensan.mercadona.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping(path = "admin")
public class AdminController {

    private final AuthenticationService authenticationService;
    private final CategoryService categoryService;
    private final ProductService productService;

    public AdminController(AuthenticationService authenticationService, CategoryService categoryService, ProductService productService) {
        this.authenticationService = authenticationService;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    @RequestMapping(value = { "", "/" })
    public String dashboard(Model model) {
        // User data
        User connectedUser = authenticationService.getAuthenticatedUser();
        model.addAttribute("userName", connectedUser.getUserName());

        // Page data
        model.addAttribute("page", "dashboard");

        return "back/components/template";
    }

    @GetMapping
    @RequestMapping(value = { "products", "products/" })
    public String products(Model model) {
        // User data
        User connectedUser = authenticationService.getAuthenticatedUser();
        model.addAttribute("userName", connectedUser.getUserName());

        // Page data
        model.addAttribute("page", "products");
        model.addAttribute("products", productService.getAllProductsById());

        return "back/components/template";
    }

    @GetMapping
    @RequestMapping(value = { "categories", "categories/" })
    public String categories(Model model) {
        // User data
        User connectedUser = authenticationService.getAuthenticatedUser();
        model.addAttribute("userName", connectedUser.getUserName());

        // Page data
        model.addAttribute("page", "categories");
        model.addAttribute("categories", categoryService.getAllCategoriesById());
        model.addAttribute("articleCount", categoryService.articlesByCategoryId());

        return "back/components/template";
    }

    @GetMapping
    @RequestMapping(value = { "users", "users/" })
    public String users(Model model) {
        // User data
        User connectedUser = authenticationService.getAuthenticatedUser();
        model.addAttribute("userName", connectedUser.getUserName());

        // Page data
        model.addAttribute("page", "users");

        return "back/components/template";
    }

}
