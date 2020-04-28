package com.erickmp.onlinestore.customer.domain.repository;

import com.erickmp.onlinestore.customer.domain.repository.entity.Customer;
import com.erickmp.onlinestore.customer.domain.repository.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    public List<Customer> findByRegion(Region region);
}
