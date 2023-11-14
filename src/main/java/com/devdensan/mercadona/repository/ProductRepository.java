package com.devdensan.mercadona.repository;

import com.devdensan.mercadona.model.Category;
import com.devdensan.mercadona.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Integer> {
    List<Product> findByCategory(Optional<Category> category);
    default boolean existsByProductId(Integer id) {
        return findById(id).isPresent();
    }

    @Query("SELECT p FROM Product p ORDER BY p.productId")
    List<Product> findAllOrderedByProductId();
}
