package com.example.zipcode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

@org.springframework.stereotype.Service
public class Service {
  @Autowired private final LocationRepository locationRepository;
  @Autowired private final PlacesRepository placesRepository;
  RestTemplate restTemplate = new RestTemplate();
  @Autowired private ModelMapper modelMapper;

  @Autowired private CountryCapitalClient countryCapitalClient;

  public Service(LocationRepository locationRepository, PlacesRepository placesRepository) {
    this.locationRepository = locationRepository;
    this.placesRepository = placesRepository;
  }

  public LocationDTO getZipcodeData(Integer zipcode) {

    // Return data from db if already present, otherwise invoke 3rd party API
    // Boolean dataPresent = false;
    LocationDAO locationDAO = null;
    PlacesDAO placesDAO = null;
    LocationDTO locationDTO = null;

    boolean isDataPresent = isDataPresent(zipcode);
    if (isDataPresent) {
      locationDAO = getLocationById(zipcode);
      placesDAO = getPlacesById(zipcode);
      locationDTO = locationDAOToDTO(locationDAO);
      PlacesDTO placesDTO = placesDAOToDTO(placesDAO);
      locationDTO.setPlacesDTOList(Arrays.asList(placesDTO));
      return locationDTO;
    } else {
      // data not present in db, call api and store in db before returning data to client
      String uri = "https://api.zippopotam.us/us/" + zipcode;
      locationDTO = restTemplate.getForObject(uri, LocationDTO.class);
      LocationDAO locationDAO1 = locationDTOToDAO(locationDTO);
      List<PlacesDTO> placesDTOList = locationDTO.getPlacesDTOList();
      List<PlacesDAO> placesDAOList =
          placesDTOList.stream()
              .map(places -> placesDTOToDAO(places, locationDAO1.getZipCode()))
              .collect(Collectors.toList());
      placesDAOList.stream().forEach(this::persistPlacesData);
      persistLocationData(locationDAO1);

      // invoke the soap webservice
      CapitalCityResponse response = countryCapitalClient.getCountryCapital("US");
      locationDTO.setCountryCapital(response.getCapitalCityResult());
      return locationDTO;
    }
  }

  // method to check if data present in db and return boolean
  public Boolean isDataPresent(Integer zipcode) {
    if ((locationRepository.findById(zipcode).isPresent())
        && (placesRepository.findById(zipcode).isPresent())) {
      return true;
    }
    return false;
  }
  // method to get location data from repo
  public LocationDAO getLocationById(Integer zipcode) {

    return locationRepository.findById(zipcode).orElse(null);
  }
  // method to get places data from repo
  public PlacesDAO getPlacesById(Integer zipcode) {

    return placesRepository.findById(zipcode).orElse(null);
  }
  // method to save location data in db
  void persistLocationData(LocationDAO location) {
    locationRepository.save(location);
  }
  // method to save places data in db
  void persistPlacesData(PlacesDAO places) {
    placesRepository.save(places);
  }
  // mapper for location dao to location dto
  LocationDTO locationDAOToDTO(LocationDAO locationDAO) {
    LocationDTO locationDTO = this.modelMapper.map(locationDAO, LocationDTO.class);
    return locationDTO;
  }
  // mapper for location dto to location dao
  LocationDAO locationDTOToDAO(LocationDTO locationDTO) {
    LocationDAO locationDAO = this.modelMapper.map(locationDTO, LocationDAO.class);
    return locationDAO;
  }
  // mapper for places dto to places dao
  PlacesDAO placesDTOToDAO(PlacesDTO placesDTO, Integer zipcode) {
    PlacesDAO placesDAO = this.modelMapper.map(placesDTO, PlacesDAO.class);
    placesDAO.setPostCode(zipcode);
    return placesDAO;
  }
  // mapper for places dao to places dto
  PlacesDTO placesDAOToDTO(PlacesDAO placesDAO) {
    PlacesDTO placesDTO = this.modelMapper.map(placesDAO, PlacesDTO.class);
    return placesDTO;
  }
}
