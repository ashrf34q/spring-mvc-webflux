package com.springframework.springwebfluxrest.bootstrap;

import com.springframework.springwebfluxrest.domain.Category;
import com.springframework.springwebfluxrest.domain.Vendor;
import com.springframework.springwebfluxrest.repositories.CategoryRepository;
import com.springframework.springwebfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public DataLoader(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }


    @Override
    public void run(String... args) throws NullPointerException {
        if(categoryRepository.count().block() == 0){
            System.out.println("Loading data on startup.");

            // Nicer than setter methods
            categoryRepository.save(Category.builder().description("Nuts").build()).block();
            categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Vegetables").build()).block();

            System.out.println("Loaded categories: " + categoryRepository.count().block());

            vendorRepository.save(Vendor.builder().firstName("Vitalik").lastName("Buterin").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Ashraf").lastName("MK").build()).block();

            System.out.println("Loaded vendors: " + vendorRepository.count().block());
        }
    }
}
