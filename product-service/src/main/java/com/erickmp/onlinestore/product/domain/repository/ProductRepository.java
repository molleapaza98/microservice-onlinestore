package com.erickmp.onlinestore.product.domain.repository;

import com.erickmp.onlinestore.product.domain.repository.entity.Category;
import com.erickmp.onlinestore.product.domain.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    public List<Product> findByCategory(Category category);
}
