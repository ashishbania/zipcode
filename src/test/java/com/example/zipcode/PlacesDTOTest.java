package com.example.zipcode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@Tag("PlacesDTOTest")
@ExtendWith(MockitoExtension.class)
class PlacesDTOTest implements ModelTestInterface {
@RepeatedTest(5)
@DisplayName("This is a repeated Test")
@ParameterizedTest
    void repeatedPlacesDTOTest(){
        //todo
    }

}