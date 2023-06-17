package com.example.zipcode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ControllerTest {

  @InjectMocks Controller controller;
  @Mock Service service;

  @BeforeEach
  void setUp() {
    this.controller = new Controller(this.service);
  }

  @Test
  void testGetZipcode() {
    when(service.getZipcodeData(123)).thenReturn(new LocationDTO());
    controller.getZipcode(123);
    verify(service, times(1)).getZipcodeData(123);
  }
}
