package com.devdensan.mercadona.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", updatable = false)
    private int categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "slug")
    private String slug;

    public Category() {
    }

    public Category(String categoryName, String slug) {
        this.categoryName = categoryName;
        this.slug = slug;
    }

    public Category(int categoryId, String categoryName, String slug) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.slug = slug;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }
}

