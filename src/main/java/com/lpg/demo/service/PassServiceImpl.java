package com.lpg.demo.service;

import com.lpg.demo.model.CustomerEntity;
import com.lpg.demo.model.PassEntity;
import com.lpg.demo.model.PassModel;
import com.lpg.demo.repository.CustomerRepository;
import com.lpg.demo.repository.PassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PassServiceImpl implements PassService {

    final
    PassRepository passRepository;

    final
    CustomerService customerService;

    public PassServiceImpl(PassRepository passRepository, CustomerService customerService) {
        this.passRepository = passRepository;
        this.customerService = customerService;
    }

    @Override
    public PassEntity createPass(PassEntity passEntity) {
        return passRepository.save(passEntity);
    }

    @Override
    public Optional<PassEntity> getPass(String pass_id) {
        return passRepository.findById(pass_id);
    }

    @Override
    public List<PassEntity> getAllPasses() {
        return passRepository.findAll();
    }

    @Override
    public PassEntity addPass(PassModel passModel) {
        Optional<PassEntity> passEntity  = passRepository.findById(passModel.getPass_id());
        if(passEntity.isPresent()){
            PassEntity pass = passEntity.get();
            if(pass.getExpiry_date().compareTo(new Date())>0 && pass.getCustomer()==null){
                Optional<CustomerEntity> cust = customerService.getCustomerById(passModel.getCustomer_id());
                CustomerEntity customerEntity;
                if(cust.isPresent()){
                    customerEntity = cust.get();
                    pass.setCustomer(customerEntity);
                    passRepository.save(pass);
                    return pass;
                }
            }
        }
        return null;
    }

    @Override
    public List<PassEntity> getAllValidPasses() {
        return passRepository.finaAllValidPasses();
    }

    @Override
    public void cancelPass(String pass_id) {
        passRepository.cancelPass(pass_id);
    }

    @Override
    public void deletePass(PassEntity passEntity) {
        passRepository.delete(passEntity);
    }

    @Override
    public List<PassEntity> getAllPassesByCustomer(String customer_id) {
        return passRepository.findAllByCustomerId(customer_id);
    }
}
