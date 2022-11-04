package com.springframework.springwebfluxrest.controllers;

import com.springframework.springwebfluxrest.domain.Vendor;
import com.springframework.springwebfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class VendorControllerTest {

    WebTestClient webTestClient;
    @Mock
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    /* So here we're basically connecting our webTestClient
         to our flux application using mock request and response objects. Same thing
         applies for all controller methods tested in our application */

    @Test
    void listAll() {
        BDDMockito.given(vendorController.listAll()).willReturn(Flux.just(Vendor.builder().firstName("Ragnar").build(),
                Vendor.builder().firstName("Bjorn").build()));

        webTestClient.get().uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getVendorById() {
        BDDMockito.given(vendorController.getVendorById("23")).willReturn(
                Mono.just(Vendor.builder().firstName("Lagertha").build()));

        webTestClient.get().uri("/api/v1/vendors/23")
                .exchange()
                .expectBody(Vendor.class);
    }
}