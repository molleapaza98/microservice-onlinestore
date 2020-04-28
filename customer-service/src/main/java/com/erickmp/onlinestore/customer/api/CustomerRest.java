package com.erickmp.onlinestore.customer.api;

import com.erickmp.onlinestore.customer.domain.CustomerService;
import com.erickmp.onlinestore.customer.domain.repository.entity.Customer;
import com.erickmp.onlinestore.customer.domain.repository.entity.Region;
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
@RequestMapping("/customers")
public class CustomerRest {

    private final CustomerService customerService;

    @Autowired
    public CustomerRest(CustomerService customerService) {
        this.customerService = customerService;
    }

    //-------------------Retrieve All customer------------------------------------------

    @GetMapping
    public ResponseEntity<List<Customer>> listAllCustomers(){
        List<Customer> customers = customerService.findCustomerAll();
        if(customers.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customers);
    }

    //-------------------Retrieve Customer by Region------------------------------------

    @GetMapping(value = "/region/{id}")
    public ResponseEntity<List<Customer>> getCustomerByRegion(@PathVariable Long id){
        log.info("Fetching Customer with Region id {}", id);
        Region region = new Region();
        region.setId(id);
        List<Customer> customers = customerService.findCustomersByRegion(region);
        if(customers == null){
            log.error("Customers with Region id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customers);
    }
    //-------------------Retrieve Single Customer--------------------------------------
    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id){
        Customer customer = customerService.getCustomer(id);
        if(customer==null){
            log.error("Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }
    //-------------------Create a Customer---------------------------------------------
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer, BindingResult result) {
        log.info("Creating Customer : {}", customer);
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Customer customerDB = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDB);
    }

    //-------------------Update a Customer---------------------------------------------
    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer){
        log.info("Updating Customer with id {}", id);
        Customer currentCustomer = customerService.getCustomer(id);
        if (currentCustomer == null){
            log.error("Unable to update. Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        currentCustomer= customerService.updateCustomer(customer);
        return ResponseEntity.ok(currentCustomer);
    }
    //-------------------Delete a Customer---------------------------------------------
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable Long id){
        log.info("Fetching & Deleting Customer with id {}", id);
        Customer customer = customerService.getCustomer(id);
        if (customer == null){
            log.error("Unable to delete. Customer with id {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        customer = customerService.deleteCustomer(customer);
        return ResponseEntity.ok(customer);
    }


    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String>  error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
