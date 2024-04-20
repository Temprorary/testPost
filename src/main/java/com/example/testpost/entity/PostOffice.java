package com.example.testpost.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PRIVATE)
public class PostOffice {

    @Id
    private Long index;

    private String name;

    private String address;

    public PostOffice(Long index, String name, String address) {
        this.index = index;
        this.name = name;
        this.address = address;
    }

    public PostOffice() {

    }
}
