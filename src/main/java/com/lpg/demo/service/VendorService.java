package com.lpg.demo.service;

import com.lpg.demo.model.VendorEntity;

import java.util.Optional;

public interface VendorService {

    public VendorEntity createVendor(VendorEntity vendorEntity);

    public Optional<VendorEntity> getVendor(String vendorId);

}
