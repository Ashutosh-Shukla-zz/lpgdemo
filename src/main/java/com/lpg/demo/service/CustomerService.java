package com.lpg.demo.service;

import com.lpg.demo.model.CustomerEntity;

import java.util.Optional;

public interface CustomerService {
    CustomerEntity createCustomer(CustomerEntity customerEntity);

    Optional<CustomerEntity> getCustomerById(String customer_id);
}
