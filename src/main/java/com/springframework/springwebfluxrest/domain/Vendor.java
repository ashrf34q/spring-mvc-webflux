package com.springframework.springwebfluxrest.domain;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Builder
public class Vendor {
    @Id
    private String id;

    private String firstName, lastName;
}
