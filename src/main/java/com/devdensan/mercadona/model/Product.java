package com.devdensan.mercadona.model;

import jakarta.persistence.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", updatable = false)
    private int productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "price")
    private float price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion; // Can be null

    public Product() {
    }

    public Product(String productName, String description, String image, float price, Category category, Promotion promotion) {
        this.productName = productName;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
        this.promotion = promotion;
    }

    public Product(int productId, String productName, String description, String image, float price, Category category, Promotion promotion) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.image = image;
        this.price = price;
        this.category = category;
        this.promotion = promotion;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int product_id) {
        this.productId = product_id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String product_name) {
        this.productName = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public String getImagePath() {
        Path path = Paths.get("src/main/resources/static/images/products/" + image);
        String imagePath = "/images/products/" + image;
        if (!Files.exists(path)) {
            imagePath = "/images/app/not-found.png";
        }
        return imagePath;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return String.format("%.2f €", price);
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String getPriceAfterPromotion() {
        if (promotion == null) {
            return null;
        } else {
            float result = price - (price * promotion.getDiscountPercentage() / 100.0f);
            return String.format("%.2f €", result);
        }
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", promotion=" + promotion +
                '}';
    }
}
