package com.example.zipcode;

import org.junit.jupiter.api.BeforeEach;

public interface ModelTestInterface {
    @BeforeEach
    default void BeforeEachMethod() {
        System.out.println("THis is a default method for model test interface");
    }
}
