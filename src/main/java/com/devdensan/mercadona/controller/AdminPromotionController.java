package com.devdensan.mercadona.controller;

import com.devdensan.mercadona.auth.AuthenticationService;
import com.devdensan.mercadona.model.Product;
import com.devdensan.mercadona.model.Promotion;
import com.devdensan.mercadona.repository.ProductRepository;
import com.devdensan.mercadona.service.PromotionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "admin/promotion")
public class AdminPromotionController {
    private final AuthenticationService authenticationService;
    private final PromotionService service;
    private final ProductRepository productRepository;

    public AdminPromotionController(AuthenticationService authenticationService, PromotionService service, ProductRepository productRepository) {
        this.authenticationService = authenticationService;
        this.service = service;
        this.productRepository = productRepository;
    }

    @GetMapping("product-id/{productId}")
    public String promotionForm(@PathVariable int productId, Model model) {
        authenticationService.loadConnectedUser(model);

        // Page data
        model.addAttribute("productId", productId);
        model.addAttribute("page", "promotion-form");
        Promotion promotion = service.getPromotionByProductId(productId);
        model.addAttribute("promotion", promotion);

        return "back/components/template";
    }

    @PostMapping("product-id/{productId}")
    public String promotionEdit(@PathVariable int productId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String percentageParam = request.getParameter("percentage");
        String startDateParam = request.getParameter("start_date");
        String endDateParam = request.getParameter("end_date");

        Map<String, String> message = new HashMap<>();
        if (percentageParam.isEmpty() && startDateParam.isEmpty() && endDateParam.isEmpty()) {
            boolean deletedPromo = service.deletePromotionFromProductId(productId);

            if (deletedPromo) {
                message.put("type", "success");
                message.put("text", "Le produit avec ID# "+ productId +" n'a aucune promotion");
            } else {
                message.put("type", "danger");
                message.put("text", "Erreur lors de la suppresion de la promotion");
            }
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/products";
        }

        // Check data
        int percentage;
        LocalDate startDate;
        LocalDate endDate;
        try {
            percentage = Integer.parseInt(percentageParam);
        } catch (NumberFormatException e) {
            message.put("type", "danger");
            message.put("text", "Veuillez insérer un percentage valide");
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/promotion/product-id/" + productId;
        }
        try {
            startDate = LocalDate.parse(startDateParam);
            endDate = LocalDate.parse(endDateParam);
        } catch (DateTimeParseException e) {
            message.put("type", "danger");
            message.put("text", "Le format des dates n'est pas valide");
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/promotion/product-id/" + productId;
        }

        // Check dates: startDate must be at least today / endDate must be at least one day after startDate
        LocalDate todayStart = LocalDate.now();
        if (startDate.isBefore(todayStart) || endDate.isBefore(startDate.plusDays(1))) {
            message.put("type", "danger");
            message.put("text", "Les dates ne sont pas valides");
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/admin/promotion/product-id/" + productId;
        }


        Promotion promotion = this.service.getPromotionByProductId(productId);
        Promotion newPromotion;
        if (promotion == null) {
            // If promo is null: Create
            newPromotion = service.createPromotion(percentage, startDate, endDate);
            if (newPromotion == null) {
                message.put("type", "danger");
                message.put("text", "Erreur pendant la création de la promo");
            }
        } else {
            // If promo exists: Edit
            newPromotion = service.editPromotion(promotion.getPromotionId(), percentage, startDate, endDate);
            if (newPromotion == null) {
                message.put("type", "danger");
                message.put("text", "Erreur pendant l'édition de la promo");
            }
        }

        // Save promotion on product
        Product product = productRepository.findById(productId).orElse(null);
        assert product != null;
        product.setPromotion(newPromotion);
        productRepository.save(product);

        message.put("type", "success");
        message.put("text", "La promotion du produit avec ID# "+ productId +" a été enregistrée");
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/products";
    }

    @PostMapping("delete")
    public String promotionDelete(@RequestParam("product-id") int productId, RedirectAttributes redirectAttributes) {
        Map<String, String> message = new HashMap<>();
        boolean deletedPromo = service.deletePromotionFromProductId(productId);

        if (deletedPromo) {
            message.put("type", "success");
            message.put("text", "Le produit avec ID# "+ productId +" n'a aucune promotion");
        } else {
            message.put("type", "danger");
            message.put("text", "Erreur lors de la suppresion de la promotion");
        }
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/admin/products";
    }

}
