package com.example.zipcode;

import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/zipcode")
@RestController
public class Controller {
  private final Service service;

  public Controller(Service service) {
    this.service = service;
  }

  @GetMapping(value = "/{zipcode}")
  public LocationDTO getZipcode(@PathVariable("zipcode") Integer zipcode) {
    return service.getZipcodeData(zipcode);
  }
}
