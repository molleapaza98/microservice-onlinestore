package com.erickmp98.onlinestore.product.domain.repository;

import com.erickmp98.onlinestore.product.domain.repository.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
