package com.springframework.springwebfluxrest.controllers;

import com.springframework.springwebfluxrest.domain.Category;
import com.springframework.springwebfluxrest.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


class CategoryControllerTest {

    WebTestClient webTestClient;
    @Mock
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void listAll() {
        BDDMockito.given(categoryController.listAll()).willReturn(Flux.just(Category.builder().description("Fruits").build(),
                Category.builder().description("Nuts").build()));

        webTestClient.get()
                .uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        BDDMockito.given(categoryController.getById("2431"))
                .willReturn(Mono.just(Category.builder().description("Nuts").build()));

        webTestClient.get()
                .uri("/api/v1/categories/2431")
                .exchange()
                .expectBody(Category.class);
    }
}