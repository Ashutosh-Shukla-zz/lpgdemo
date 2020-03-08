package com.lpg.demo.repository;

import com.lpg.demo.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository  extends JpaRepository<CustomerEntity, String> {
}
