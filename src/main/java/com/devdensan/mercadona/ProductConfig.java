package com.devdensan.mercadona;

import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.model.Product;
import com.devdensan.mercadona.model.Promotion;
import com.devdensan.mercadona.repository.CategoryRepository;
import com.devdensan.mercadona.repository.ProductRepository;
import com.devdensan.mercadona.repository.PromotionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class ProductConfig {

    @Bean
    CommandLineRunner commandLineRunner(CategoryRepository categoryRepository, ProductRepository productRepository, PromotionRepository promotionRepository) {
        return args -> {
            Category boissons = new Category("Boissons");
            Category gateaux = new Category("Gâteaux");
            Promotion promo1 = new Promotion(10, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
            Product p1 = new Product("Lait Bonnelait", "Bouteille 750ml",
                    "lait-bonnelait.png", 1.95f, boissons, null);
            Product p2 = new Product("Bjorg", "Boisson biologique à base d'amandes, sans sucres, Stérilisée UHT",
                    "lait-bjorg.jpg", 1.77f, boissons, promo1);
            Product p3 = new Product("Pâturages", "Lait demi-écrémé stérilisé UHT origine France",
                    "lait-paturages.jpg", 5.7f, boissons, null);
            Product p4 = new Product("Les Eleveurs vous disent Merci", "Lait demi-écrémé stérilisé UHT origine France",
                    "lait-merci.jpg", 6.6f, boissons, null);
            Product p5 = new Product("Candia", "Le lait sans lactose",
                    "lait-candia.jpg", 7.97f, boissons, null);
            Product p6 = new Product("Chabrior", "Gâteaux Snack'Lait cacao",
                    "soda-field.png", 1.94f, gateaux, null);

            categoryRepository.saveAll(
                    List.of(boissons, gateaux)
            );
            promotionRepository.save(promo1);
            productRepository.saveAll(
                    List.of(p1, p2, p3, p4, p5, p6)
            );
        };
    }
}
