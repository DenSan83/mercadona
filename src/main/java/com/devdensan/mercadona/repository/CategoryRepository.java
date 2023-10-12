package com.devdensan.mercadona.repository;

import com.devdensan.mercadona.model.Category;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository
        extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.slug = :slug")
    Category findCategoryByName(@Param("slug") String slug);
}
