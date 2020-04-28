package com.erickmp.onlinestore.customer.domain;

import com.erickmp.onlinestore.customer.domain.repository.entity.Customer;
import com.erickmp.onlinestore.customer.domain.repository.entity.Region;

import java.util.List;

public interface CustomerService {

    public List<Customer> findCustomerAll();
    public List<Customer> findCustomersByRegion(Region region);

    public Customer createCustomer(Customer customer);
    public Customer updateCustomer(Customer customer);
    public Customer deleteCustomer(Customer customer);
    public Customer getCustomer(Long id);


}
