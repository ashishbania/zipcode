package com.example.zipcode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"place name", "longitude", "state", "state abbreviation", "latitude"})
public class PlacesDTO {
  @JsonProperty("place name")
  String placeName;

  String longitude;
  String state;

  @JsonProperty("state abbreviation")
  String stateCode;

  String latitude;
}
