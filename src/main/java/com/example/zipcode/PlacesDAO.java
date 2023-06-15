package com.example.zipcode;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PlacesDAO {
  @Id Integer postCode;
  String placeName;

  String longitude;
  String state;

  String stateCode;

  String latitude;
}
