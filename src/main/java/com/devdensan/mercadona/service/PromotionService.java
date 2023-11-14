package com.devdensan.mercadona.service;

import com.devdensan.mercadona.model.Product;
import com.devdensan.mercadona.model.Promotion;
import com.devdensan.mercadona.repository.ProductRepository;
import com.devdensan.mercadona.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PromotionService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    @Autowired
    public PromotionService(ProductRepository productRepository, PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }

    public Promotion getPromotionByProductId(int productId) {
        // Verify if product exist
        boolean productExists = this.productRepository.existsByProductId(productId);
        if (!productExists) {
            return null;
        }

        Product product = this.productRepository.findById(productId).orElse(null);
        assert product != null;
        return product.getPromotion();
    }

    public Promotion createPromotion(int percentage, LocalDate startDate, LocalDate endDate) {
        Promotion p = new Promotion(percentage, startDate, endDate);
        return promotionRepository.save(p);
    }

    public Promotion editPromotion(int promotionId, int percentage, LocalDate startDate, LocalDate endDate) {
        Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
        if (promotion != null) {
            promotion.setDiscountPercentage(percentage);
            promotion.setStartDate(startDate);
            promotion.setEndDate(endDate);

            promotionRepository.save(promotion);
        }
        return promotion;
    }

    public boolean deletePromotionFromProductId(int productId) {
        // Delete from product
        Product product = productRepository.findById(productId).orElse(null);
        assert product != null;
        Promotion promotion = product.getPromotion();
        product.setPromotion(null);
        productRepository.save(product);

        if (promotion == null) {
            return true;
        }

        // Delete from database
        try {
            promotionRepository.deleteById(promotion.getPromotionId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
