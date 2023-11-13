package com.devdensan.mercadona.repository;

import com.devdensan.mercadona.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository
        extends JpaRepository<Category, Integer> {

    Optional<Category> findById(Integer id);
    default boolean existsByCategoryId(Integer id) {
        return findById(id).isPresent();
    }

    @Query("SELECT c FROM Category c ORDER BY c.categoryId")
    List<Category> findAllOrderedByCategoryId();
}
