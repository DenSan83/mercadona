package com.devdensan.mercadona.repository;

import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Integer> {
    List<Product> findByCategory(Category category);
}
