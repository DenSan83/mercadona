package com.devdensan.mercadona.controller;

import com.devdensan.mercadona.auth.AuthenticationService;
import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "admin/category")
public class AdminCategoryController {
    private final AuthenticationService authenticationService;
    private final CategoryService service;

    public AdminCategoryController(AuthenticationService authenticationService, CategoryService service) {
        this.authenticationService = authenticationService;
        this.service = service;
    }

    @GetMapping("new")
    public String categoryForm(Model model) {
        authenticationService.loadConnectedUser(model);

        // Page data
        model.addAttribute("page", "category-form");

        return "back/components/template";
    }

    @PostMapping("new")
    public String categoryAdd(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String categoryName = request.getParameter("category-name");
        Map<String, String> message = new HashMap<>();
        message.put("type", "danger");
        if (categoryName.equals("")) {
            message.put("text", "Erreur : veuillez renseigner un nom de catégorie");

            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/category/new";
        }

        if (service.existsByName(categoryName)) {
            message.put("text", "Une catégorie existe déjà avec ce nom");
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/category/new";
        }

        Category cat = service.newCategory(request, redirectAttributes);
        if (cat == null) {
            message.put("text", "Erreur sur l'ajout de la catégorie");
        } else {
            message.put("type", "success");
            message.put("text", "Catégorie ajoutée correctement");
        }
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{categoryId}")
    public String editCategory(@PathVariable int categoryId, Model model) {
        authenticationService.loadConnectedUser(model);
        Category category = service.getCategoryById(categoryId);
        model.addAttribute("category", category);

        model.addAttribute("page", "category-form");
        return "back/components/template";
    }

    @PostMapping("/edit/{categoryId}")
    public String saveEditedCategory(@PathVariable int categoryId, @RequestParam("category-name") String newName, RedirectAttributes redirectAttributes) {
        Map<String, String> message = new HashMap<>();
        message.put("type", "danger");

        if (service.existsByName(newName)) {
            message.put("text", "Une catégorie existe déjà avec ce nom");
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/category/edit/" + categoryId;
        }

        Category editedCategory = service.editCategory(categoryId, newName);
        if (editedCategory == null) {
            message.put("text", "Erreur sur la modification de la catégorie");
        } else {
            message.put("type", "success");
            message.put("text", "Catégorie modifiée correctement");
        }
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/admin/categories";
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam("category-id") int categoryId, RedirectAttributes redirectAttributes) {
        Map<String, String> message = new HashMap<>();
        message.put("type", "danger");

        // Verify if exists
        boolean categoryExists = service.existsByCategoryId(categoryId);
        if (!categoryExists) {
            message.put("text", "Catégorie non trouvée ou inéxistante.");

            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/categories";
        }

        // Verify if empty
        Map<Integer, Integer> categories =  service.articlesByCategoryId();
        Integer categoryCount = categories.get(categoryId);

        if (categoryCount != 0) {
            message.put("text", "La catégorie a encore des produits. Veuillez les retirer avant de supprimer la catégorie.");
        } else {

            boolean deleted = service.deleteCategory(categoryId);
            if (deleted) {
                message.put("type", "success");
                message.put("text", "Catégorie supprimée correctement");
            } else {
                message.put("text", "Erreur lors de la suppression de la catégorie");
            }
        }

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/categories";
    }

}
