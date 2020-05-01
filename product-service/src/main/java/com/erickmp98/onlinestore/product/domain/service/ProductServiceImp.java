package com.erickmp98.onlinestore.product.domain.service;

import com.erickmp98.onlinestore.product.domain.ProductService;
import com.erickmp98.onlinestore.product.domain.repository.CategoryRepository;
import com.erickmp98.onlinestore.product.domain.repository.ProductRepository;
import com.erickmp98.onlinestore.product.domain.repository.entity.Category;
import com.erickmp98.onlinestore.product.domain.repository.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public List<Product> findProductAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        Product productDB = getProduct(product.getId());
        if(productDB != null){
            return productDB;
        }
        product.setStatus("CREATED");
        product.setCreateAt(new Date());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product productDB = getProduct(product.getId());
        if(productDB == null){
            return null;
        }
        productDB.setName(product.getName());
        productDB.setDescription(product.getDescription());
        productDB.setCategory(product.getCategory());
        productDB.setPrice(product.getPrice());
        return productRepository.save(productDB);
    }

    @Override
    public Product deleteProduct(Long id) {
        Product productDB = getProduct(id);
        if(productDB == null){
            return null;
        }
        productDB.setStatus("DELETED");
        return productRepository.save(productDB);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) {
        Product productDB = getProduct(id);
        if (null == productDB){
            return null;
        }
        Double stock = productDB.getStock() + quantity;
        productDB.setStock(stock);
        return productRepository.save(productDB);
    }

    //---CATEGORY------------------------------------

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        Category categoryDB = getCategory(category.getId());
        if(categoryDB == null){
            return  null;
        }
        categoryDB.setName(category.getName());
        return categoryRepository.save(categoryDB);
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> findCategoryAll() {
        return categoryRepository.findAll();
    }
}
