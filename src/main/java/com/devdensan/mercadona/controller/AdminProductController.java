package com.devdensan.mercadona.controller;

import com.devdensan.mercadona.auth.AuthenticationService;
import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.model.Product;
import com.devdensan.mercadona.model.User;
import com.devdensan.mercadona.service.CategoryService;
import com.devdensan.mercadona.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "admin/product")
public class AdminProductController {
    private final AuthenticationService authenticationService;
    private final CategoryService categoryService;
    private final ProductService service;

    public AdminProductController(AuthenticationService authenticationService, CategoryService categoryService, ProductService service) {
        this.authenticationService = authenticationService;
        this.categoryService = categoryService;
        this.service = service;
    }

    @GetMapping("new")
    public String productForm(Model model) {
        // User data
        User connectedUser = authenticationService.getAuthenticatedUser();
        model.addAttribute("userName", connectedUser.getUserName());

        // Page data
        model.addAttribute("page", "product-form");
        model.addAttribute("categories", categoryService.getAllCategoriesById());

        return "back/components/template";
    }

    @PostMapping("new")
    public String productAdd(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        int categoryId = Integer.parseInt(request.getParameter("category"));
        boolean categoryExists = categoryService.existsByCategoryId(categoryId);
        Map<String, String> message = new HashMap<>();
        if (!categoryExists) {
            message.put("type", "danger");
            message.put("text", "Erreur : catégorie inéxistante");

            return "redirect:/admin/products";
        }

        // Check parameters
        String newName = request.getParameter("product-name");
        String description = request.getParameter("description");
        Category category = categoryService.getCategoryById(categoryId);
        float price;
        try {
            price = Float.parseFloat(request.getParameter("price"));
        } catch (NumberFormatException e) {
            price = 0;
        }

        if (newName.equals("") || description.equals("") || price == 0) {
            message.put("type", "danger");
            message.put("text", "Erreur : Veuillez remplir tous les champs");

            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/products";
        }

        Product product = service.newProduct(request);
        if (product == null) {
            message.put("type", "danger");
            message.put("text", "Erreur sur l'ajout du produit");
        } else {
            message.put("type", "success");
            message.put("text", "Produit ajoutée correctement");
        }
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{productId}")
    public String editProduct(@PathVariable int productId, Model model) {
        Product product = service.getProductById(productId);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategoriesById());

        model.addAttribute("page", "product-form");
        return "back/components/template";
    }

    @PostMapping("/edit/{productId}")
    public String saveEditedProduct(@PathVariable int productId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Verify if category exists
        Map<String, String> message = new HashMap<>();
        int categoryId = Integer.parseInt(request.getParameter("category"));
        boolean categoryExists = categoryService.existsByCategoryId(categoryId);
        if (!categoryExists) {
            message.put("type", "danger");
            message.put("text", "Erreur : catégorie inéxistante");

            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/products";
        }

        // Verify if product exists
        Product product = service.getProductById(productId);
        if (product == null) {
            message.put("type", "danger");
            message.put("text", "Erreur : produit inéxistant");

            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/products";
        }

        // Check parameters
        String newName = request.getParameter("product-name");
        String description = request.getParameter("description");
        Category category = categoryService.getCategoryById(categoryId);
        float price;
        try {
            price = Float.parseFloat(request.getParameter("price"));
        } catch (NumberFormatException e) {
            price = 0;
        }

        if (newName.equals("") || description.equals("") || price == 0) {
            message.put("type", "danger");
            message.put("text", "Erreur : Veuillez remplir tous les champs");

            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/products";
        }

        // Product edition
        Product editedProduct = service.editProduct(productId, newName, description, category, price);
        if (editedProduct == null) {
            message.put("type", "danger");
            message.put("text", "Erreur sur la modification du produit");
        } else {
            message.put("type", "success");
            message.put("text", "Produit modifiée correctement");
        }
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/products";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam("product-id") int productId, RedirectAttributes redirectAttributes) {
        Map<String, String> message = new HashMap<>();

        // Verify if exists
        boolean productExists = service.existsByProductId(productId);
        if (!productExists) {
            message.put("type", "danger");
            message.put("text", "Erreur : produit non trouvé ou inéxistant.");

            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/products";
        }

        boolean deleted = service.deleteProduct(productId);
        if (deleted) {
            message.put("type", "success");
            message.put("text", "Produit supprimé correctement.");
        } else {
            message.put("type", "danger");
            message.put("text", "Erreur lors de la suppression du produit.");
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/products";
    }
}
