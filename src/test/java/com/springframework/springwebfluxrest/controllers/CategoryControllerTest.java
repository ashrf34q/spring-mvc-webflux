package com.springframework.springwebfluxrest.controllers;

import com.springframework.springwebfluxrest.domain.Category;
import com.springframework.springwebfluxrest.repositories.CategoryRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        given(categoryController.listAll()).willReturn(Flux.just(Category.builder().description("Fruits").build(),
                Category.builder().description("Nuts").build()));

        webTestClient.get()
                .uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(categoryController.getById("2431"))
                .willReturn(Mono.just(Category.builder().description("Nuts").build()));

        webTestClient.get()
                .uri("/api/v1/categories/2431")
                .exchange()
                .expectBody(Category.class);
    }


    @Test
    void createVendor(){
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().description("Nuts").build()));

        Mono<Category> catToSave = Mono.just(Category.builder().description("Some category").build());

        webTestClient.post().uri("/api/v1/categories")
                .body(catToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void updateCategory(){

        // This acts just like the static when() method in mockMVC testing
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToSave = Mono.just(Category.builder().description("Some category").build());

        webTestClient.put().uri("/api/v1/categories/234")
                .body(catToSave, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testPatchWithChanges() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate = Mono.just(Category.builder().description("Clothing").build());

        webTestClient.patch().uri("/api/v1/categories/24")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testPatchNoChanges(){
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate = Mono.just(Category.builder().build());

        webTestClient.patch().uri("/api/v1/categories/24")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, times(0)).save(any(Category.class));
    }
}