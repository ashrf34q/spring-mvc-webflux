package com.springframework.springwebfluxrest.controllers;

import com.springframework.springwebfluxrest.domain.Vendor;
import com.springframework.springwebfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


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
        given(vendorController.listAll()).willReturn(Flux.just(Vendor.builder().firstName("Ragnar").build(),
                Vendor.builder().firstName("Bjorn").build()));

        webTestClient.get().uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getVendorById() {
        given(vendorController.getVendorById("23")).willReturn(
                Mono.just(Vendor.builder().firstName("Lagertha").build()));

        webTestClient.get().uri("/api/v1/vendors/23")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void postVendor(){
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("Floki").build());

        webTestClient.post().uri("/api/v1/vendors")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void updateVendors(){
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("Aethelwuf").build());

        webTestClient.put().uri("/api/v1/vendors/3542")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void patchVendorsWithChanges() {
        given(vendorRepository.findById(anyString())).willReturn(Mono.just(Vendor.builder().build()));

        given(vendorRepository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorMonoToSave = Mono.just(Vendor.builder().firstName("Ketill").build());

        webTestClient.patch().uri("/api/v1/vendors/2353")
                .body(vendorMonoToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }
}