package com.lpg.demo.controller;

import com.lpg.demo.model.CustomerEntity;
import com.lpg.demo.model.PassEntity;
import com.lpg.demo.model.PassModel;
import com.lpg.demo.model.VendorEntity;
import com.lpg.demo.service.CustomerService;
import com.lpg.demo.service.PassService;
import com.lpg.demo.service.VendorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(path = "/v1")
public class PassController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    final
    PassService passService;

    final
    VendorService vendorService;

    final
    CustomerService customerService;

    public PassController(PassService passService, VendorService vendorService, CustomerService customerService) {
        this.passService = passService;
        this.vendorService = vendorService;
        this.customerService = customerService;
    }

    @GetMapping(path = "/allpasses", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllPasses() {
        log.info("Inside get /self mapping");
        List<PassEntity> passEntities = new ArrayList<>();
        passEntities = passService.getAllValidPasses();
        return new ResponseEntity<>(passEntities, HttpStatus.OK);
    }

    @PostMapping("/addpass")
    public ResponseEntity<Object> addPass(@Valid @RequestBody PassModel passModel) {
        log.info("Inside post /addPass mapping");
        HashMap<String, Object> entities = new HashMap<String, Object>();
        PassEntity passEntity = passService.addPass(passModel);
        if (passEntity != null) {
            return new ResponseEntity<>(passEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot Add Pass, Customer or Pass doesnt exist", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/cancelpass")
    public ResponseEntity<Object> cancelPass(@Valid @RequestBody PassModel passModel) {
        log.info("Inside post /addPass mapping");
        HashMap<String, Object> entities = new HashMap<String, Object>();
        if (passModel.getCustomer_id() == null || passModel.getPass_id() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Optional<PassEntity> pass = passService.getPass(passModel.getPass_id());
        PassEntity passEntity;
        if (pass.isPresent()) {
            passEntity = pass.get();
            if (passEntity.getCustomer() != null && passEntity.getCustomer().getCustomer_id().equalsIgnoreCase(passModel.getCustomer_id())) {
                passEntity.setUpdated_ts(new Date());
                passEntity.setCustomer(null);

                //Use the same create Pass to save updated pass
                passService.createPass(passEntity);
                return new ResponseEntity<>("Pass Cancelled", HttpStatus.OK);
            } else
                return new ResponseEntity<>("Cannot cancel pass", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Pass Doesnt exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/renewpass")
    public ResponseEntity<Object> renewPass(@Valid @RequestBody PassModel passModel) {
        log.info("Inside post /cancelPass mapping");
        HashMap<String, Object> entities = new HashMap<String, Object>();
        if (passModel.getCustomer_id() == null || passModel.getPass_id() == null || passModel.getPass_length() <= 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Optional<PassEntity> pass = passService.getPass(passModel.getPass_id());
        PassEntity passEntity;
        if (pass.isPresent()) {
            passEntity = pass.get();
            if (passEntity.getCustomer() != null && passEntity.getCustomer().getCustomer_id().equalsIgnoreCase(passModel.getCustomer_id())) {
                passEntity.setUpdated_ts(new Date());

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, passEntity.getPass_length() + passModel.getPass_length());
                java.util.Date dt = cal.getTime();
                passEntity.setExpiry_date(dt);
                passEntity.setPass_length(passModel.getPass_length());
                //Use the same create Pass to save updated pass
                PassEntity savedPass = passService.createPass(passEntity);
                return new ResponseEntity<>(savedPass, HttpStatus.OK);
            } else
                return new ResponseEntity<>("Cannot Update Pass", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("Pass not found", HttpStatus.NOT_FOUND);
    }


    @PostMapping("/pass")
    public ResponseEntity<Object> createPass(@Valid @RequestBody PassModel passModel) {
        log.info("Inside post /pass mapping");
        HashMap<String, Object> entities = new HashMap<String, Object>();

        PassEntity passEntity = new PassEntity();

        //TODO (Assumption) Expiry date to be set for 1 month from current date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, passModel.getPass_length());
        java.util.Date dt = cal.getTime();
        passEntity.setExpiry_date(dt);

        passEntity.setCreated_ts(new Date());
        passEntity.setUpdated_ts(new Date());
        passEntity.setPass_length(passModel.getPass_length());
        passEntity.setPass_city(passModel.getPass_city());
        Optional<VendorEntity> vendorEntityOptional = vendorService.getVendor(passModel.getVendor_id());
        if(vendorEntityOptional.isPresent()) {
            passEntity.setVendor(vendorEntityOptional.get());
            PassEntity savedPass = passService.createPass(passEntity);
            return new ResponseEntity<>(savedPass, HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>("Vendor not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/validatepass", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> validatePass(@RequestHeader String vendor_id, @RequestHeader String pass_id) {
        log.info("Inside get /ValidatePass mapping");

        Optional<PassEntity> pass = passService.getPass(pass_id);
        if (pass.isPresent()) {
            PassEntity passEntity = pass.get();
            if (passEntity.getVendor().getVendor_id().equalsIgnoreCase(vendor_id)) {
                return new ResponseEntity<>(null, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(path = "/pass")
    public ResponseEntity<Object> deletePass(@RequestParam String vendor_id, @RequestParam String pass_id) {
        log.info("Inside delete /pass mapping");

        Optional<PassEntity> pass = passService.getPass(pass_id);
        if (pass.isPresent()) {
            PassEntity passEntity = pass.get();
            if (passEntity.getVendor().getVendor_id().equalsIgnoreCase(vendor_id)) {
                passService.deletePass(passEntity);
                return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("Cannot delete pass", HttpStatus.FORBIDDEN);
            }
        } else
            return new ResponseEntity<>("Pass does not exist", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/mypass")
    public ResponseEntity<Object> createCustomer(@RequestHeader String customer_id) {
        log.info("Inside get /mypass mapping");
        if (!customer_id.isEmpty()) {
            Optional<CustomerEntity> cust = customerService.getCustomerById(customer_id);
            if (cust.isPresent()) {
                List<PassEntity> passEntityList = passService.getAllPassesByCustomer(customer_id);
                return new ResponseEntity<>(passEntityList, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Customer does not exist", HttpStatus.NOT_FOUND);
    }
}
