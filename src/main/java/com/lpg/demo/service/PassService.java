package com.lpg.demo.service;

import com.lpg.demo.model.PassEntity;
import com.lpg.demo.model.PassModel;

import java.util.List;
import java.util.Optional;

public interface PassService {

    public PassEntity createPass(PassEntity passEntity);
    public Optional<PassEntity> getPass(String pass_id);

    List<PassEntity> getAllPasses();

    PassEntity addPass(PassModel passModel);

    List<PassEntity> getAllValidPasses();

    void cancelPass(String pass_id);

    void deletePass(PassEntity passEntity);

    List<PassEntity> getAllPassesByCustomer(String customer_id);
}
