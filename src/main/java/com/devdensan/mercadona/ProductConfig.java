package com.devdensan.mercadona;

import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.model.Product;
import com.devdensan.mercadona.repository.CategoryRepository;
import com.devdensan.mercadona.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProductConfig {

    @Bean
    CommandLineRunner commandLineRunner(CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            Category boissons = new Category("Boissons", "boissons");
            Category gateaux = new Category("Gâteaux", "gateaux");
            Product p1 = new Product("Lait Bonnelait", "Bouteille 750ml",
                    "lait-bonnelait.png", 1.95f, boissons, null);
            Product p2 = new Product("Bjorg", "Boisson biologique à base d'amandes, sans sucres, Stérilisée UHT - la brique de 1l",
                    "lait-bjorg.png", 1.77f, boissons, null);
            Product p3 = new Product("Pâturages", "Lait demi-écrémé stérilisé UHT origine France - les 6 briques de 1 l",
                    "lait-paturages.png", 5.7f, boissons, null);
            Product p4 = new Product("Les Eleveurs Vous Disent Merci", "Lait demi-écrémé stérilisé UHT origine France - les 6 briques de 1 l",
                    "lait-merci.png", 6.6f, boissons, null);
            Product p5 = new Product("Candia", "Lait demi-écrémé stérilisé UHT - les 6 bouteilles de 1l - 6l",
                    "lait-candia.png", 7.97f, boissons, null);
            Product p6 = new Product("Chabrior", "Gâteaux Snack'Lait cacao - Les 10 gâteaux de 42 g",
                    "soda-field.png", 1.94f, gateaux, null);

            categoryRepository.saveAll(
                    List.of(boissons, gateaux)
            );
            productRepository.saveAll(
                    List.of(p1, p2, p3, p4, p5, p6)
            );
        };
    }
}
