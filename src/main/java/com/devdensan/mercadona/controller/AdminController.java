package com.devdensan.mercadona.controller;

import com.devdensan.mercadona.auth.AuthenticationService;
import com.devdensan.mercadona.model.User;
import com.devdensan.mercadona.service.CategoryService;
import com.devdensan.mercadona.service.ProductService;
import com.devdensan.mercadona.service.PromotionService;
import com.devdensan.mercadona.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "admin")
public class AdminController {

    private final AuthenticationService authenticationService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final UserService userService;

    public AdminController(AuthenticationService authenticationService, CategoryService categoryService, ProductService productService, PromotionService promotionService, UserService userService) {
        this.authenticationService = authenticationService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.promotionService = promotionService;
        this.userService = userService;
    }

    @GetMapping
    @RequestMapping(value = { "", "/" })
    public String dashboard(Model model) {
        // User data
        User connectedUser = authenticationService.getAuthenticatedUser();
        model.addAttribute("userName", connectedUser.getUserName());
        model.addAttribute("page", "dashboard");

        // Products
        Map<String, Integer> products = new HashMap<>();
        int countProducts = productService.countProducts();
        int countPromotions = promotionService.countPromotions();
        products.put("total", countProducts);
        products.put("withPromo", countPromotions);
        products.put("withoutPromo", countProducts - countPromotions);
        model.addAttribute("products", products);

        // Categories
        Map<String, Integer> categories = new HashMap<>();
        int countCategories = categoryService.countCategories();
        int countEmptyCategories = categoryService.countEmptyCategories();
        categories.put("total", countCategories);
        categories.put("notFull", countEmptyCategories);
        categories.put("full", countCategories - countEmptyCategories);
        model.addAttribute("categories", categories);

        // Users
        int countUsers = userService.countUsers();
        model.addAttribute("users", countUsers);

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
