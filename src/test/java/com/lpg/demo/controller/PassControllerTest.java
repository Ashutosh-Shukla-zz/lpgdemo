package com.lpg.demo.controller;

import com.lpg.demo.AbstractTest;
import com.lpg.demo.model.PassEntity;
import com.lpg.demo.model.PassModel;
import com.lpg.demo.model.VendorEntity;
import com.lpg.demo.service.PassService;
import com.lpg.demo.service.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PassControllerTest extends AbstractTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @InjectMocks
    PassController passController;

    @Mock
    PassService passService;


    @Mock
    VendorService vendorService;

    @Test
    public void getPassList() throws Exception {
        String uri = "/v1/allpasses";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        PassEntity[] passEntitieslist = super.mapFromJson(content, PassEntity[].class);
        assertTrue(passEntitieslist.length > 0);
    }

    @Test
    public void createPass() throws Exception {
        String uri = "/v1/pass";
        PassModel pm = new PassModel("","Test",4, null,"a204467e-a0e2-41fa-951b-3bc8b0f5943b","");
        PassEntity pass = new PassEntity();
        VendorEntity vendorEntity = new VendorEntity();
        vendorEntity.setVendor_id(pm.getVendor_id());

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(vendorService.getVendor(any())).thenReturn(java.util.Optional.of(vendorEntity));
        when(passService.createPass(any(PassEntity.class))).thenReturn(pass);

        ResponseEntity<Object> responseEntity = passController.createPass(pm);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    @AfterEach
    void tearDown() {
    }
}
