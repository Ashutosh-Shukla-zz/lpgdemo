package com.lpg.demo.controller;

import com.lpg.demo.model.VendorEntity;
import com.lpg.demo.service.VendorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.HashMap;

@Controller
@RequestMapping(path = "/v1")
public class VendorController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    final
    VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping("/vendor")
    public ResponseEntity<Object> createVendor(@Valid @RequestBody VendorEntity vendorEntity) {
        log.info("Inside post /pass mapping");
        HashMap<String, Object> entities = new HashMap<String, Object>();
        if (validateEmail(vendorEntity.getEmail_address())) {
            VendorEntity savedVendor = vendorService.createVendor(vendorEntity);
            return new ResponseEntity<>(savedVendor, HttpStatus.CREATED);
        } else {
            entities.put("Validation Error",
                    "Please input correct email id");
        }
        return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
    }

    public Boolean validateEmail(String email) {
        if (email != null || (!email.equalsIgnoreCase(""))) {
            String emailvalidator = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                    + "A-Z]{2,7}$";

            return email.matches(emailvalidator);
        }
        return Boolean.FALSE;


    }
}
