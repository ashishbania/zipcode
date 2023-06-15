package com.example.zipcode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"post code", "country", "country abbreviation"})
@Data
public class LocationDTO {
  @JsonProperty("post code")
  @JsonSerialize(using = ToStringSerializer.class)
  Integer zipCode;

  String country;

  @JsonProperty("country abbreviation")
  String countryCode;

  @JsonProperty("country capital")
  String CountryCapital;

  @JsonProperty("places")
  List<PlacesDTO> placesDTOList;

  public LocationDTO(int i, String unitedStates, String us) {

  }
}
