package com.example.zipcode;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class LocationDAO {
  @Id Integer zipCode;

  String country;
  String countryCode;
}
