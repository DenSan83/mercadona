package com.devdensan.mercadona.repository;

import com.devdensan.mercadona.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository
        extends JpaRepository<Category, Integer> {
}
