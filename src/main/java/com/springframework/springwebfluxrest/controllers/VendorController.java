package com.springframework.springwebfluxrest.controllers;

import com.springframework.springwebfluxrest.domain.Vendor;
import com.springframework.springwebfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("/api/v1/vendors")
    Flux<Vendor> listAll(){
        return vendorRepository.findAll();
    }

    @GetMapping("/api/v1/vendors/{id}")
    Mono<Vendor> getVendorById(@PathVariable String id){
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/vendors")
    Mono<Void> postVendor(@RequestBody Publisher<Vendor> vendorStream){
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/api/v1/vendors/{id}")
    Mono<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/api/v1/vendors/{id}")
    Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor vendor) throws ExecutionException, InterruptedException {
        Vendor foundVendor = vendorRepository.findById(id).toFuture().get();

        assert foundVendor != null;
        if(!Objects.equals(vendor.getFirstName(), foundVendor.getFirstName())){
            foundVendor.setFirstName(vendor.getFirstName());
        }

        else if(!Objects.equals(vendor.getLastName(), foundVendor.getLastName())) {
            foundVendor.setLastName(vendor.getLastName());
        }

        return Mono.just(foundVendor);
    }
}
