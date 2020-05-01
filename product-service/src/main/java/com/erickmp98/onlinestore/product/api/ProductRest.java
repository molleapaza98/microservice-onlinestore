package com.erickmp98.onlinestore.product.api;

import com.erickmp98.onlinestore.product.domain.ProductService;
import com.erickmp98.onlinestore.product.domain.repository.entity.Category;
import com.erickmp98.onlinestore.product.domain.repository.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductRest {

    @Autowired
    ProductService productService;

    //----------------Retrieve All Products----------------------------
    @GetMapping
    public ResponseEntity<List<Product>> listAllProducts(){
        List<Product> products = productService.findProductAll();
        if (products.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    //----------------Retrieve Product by Category---------------------
    @GetMapping(value = "/category/{id}")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable Long id){
        log.info("Fetching Product with category id {}", id);
        Category category = new Category();
        category.setId(id);
        List<Product> products = productService.findByCategory(category);
        if(products==null) {
            log.error("Products with category id {} not found", id);
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    //----------------Retrieve Single Product--------------------------
    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id){
        Product product = productService.getProduct(id);
        if (product == null) {
            log.error("Product with id {} not found", id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(product);
    }

    //----------------Create a Product---------------------------------
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result){
        log.info("Creating Product : {}", product);
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Product productCreate = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreate);
    }

    //----------------Update a Product---------------------------------
    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product){
        log.info("Updating Product : {}", product);
        Product currentProduct = productService.getProduct(id);
        if (currentProduct == null){
            log.error("Unable to update. Product with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        product.setId(id);
        productService.updateProduct(product);
        return ResponseEntity.ok(product);
    }

    //----------------Delete a Product---------------------------------------
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id){
        log.info("Fetching & Deleting Product with id {}", id);
        Product productDelete = productService.getProduct(id);
        if (productDelete == null){
            log.error("Unable to delete. Product with id {} not found.",id);
            return ResponseEntity.notFound().build();
        }
        productDelete = productService.deleteProduct(id);
        return ResponseEntity.ok(productDelete);
    }

    //----------------Update Stock to Product---------------------------------
    @PutMapping(value = "/{id}/stock/{quantity}")
    public ResponseEntity<Product> updateStockProduct(@PathVariable Long id, @PathVariable Double quantity){
        log.info("Update stock product : {} with quantity {}",id,quantity);
        Product product = productService.getProduct(id);
        if (product == null){
            log.error("Unable to update stock. Product with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        product = productService.updateStock(id,quantity);
        return ResponseEntity.status(HttpStatus.OK).body(product);

    }

    public String formatMessage(BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err->{
                    Map<String,String> error = new HashMap<>();
                    error.put(err.getField(),err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString=mapper.writeValueAsString(errorMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;
    }
}
