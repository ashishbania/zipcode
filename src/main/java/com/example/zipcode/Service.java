package com.example.zipcode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

  public Service(LocationRepository locationRepository, PlacesRepository placesRepository,ModelMapper modelMapper, CountryCapitalClient countryCapitalClient ) {
    this.locationRepository = locationRepository;
    this.placesRepository = placesRepository;
    this.modelMapper=modelMapper;
    this.countryCapitalClient=countryCapitalClient;
  }

  public LocationDTO getZipcodeData(Integer zipcode) {

    // Return data from db if already present, otherwise invoke 3rd party API
    // Boolean dataPresent = false;
    LocationDAO locationDAO = null;
    PlacesDAO placesDAO = null;
    LocationDTO locationDTO = null;

    boolean isDataPresent = isDataPresent(zipcode);
    if (isDataPresent) {
      locationDTO = getLocationDataFromDB(zipcode);
      return locationDTO;
    } else {
      // data not present in db, call api and store in db before returning data to client
      locationDTO = getLocationDataFromAPI(zipcode);
      return locationDTO;
    }
  }

  LocationDTO getLocationDataFromAPI(Integer zipcode) {
    LocationDTO locationDTO;
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

  public LocationDTO getLocationDataFromDB(Integer zipcode) {
//    Optional<LocationDAO> locationDAO;
//    PlacesDAO placesDAO;
//    LocationDTO locationDTO;
//    locationDAO = getLocationById(zipcode);
//    placesDAO = getPlacesById(zipcode);
//    locationDTO = locationDAOToDTO(locationDAO.orElse(null));
//    PlacesDTO placesDTO = placesDAOToDTO(placesDAO);
//    locationDTO.setPlacesDTOList(Arrays.asList(placesDTO));
//    return locationDTO;
    Optional<LocationDAO> locationDAO = getLocationById(zipcode);
    Optional<PlacesDAO> placesDAO = getPlacesById(zipcode);

    if (locationDAO.isPresent()) {
      LocationDTO locationDTO = locationDAOToDTO(locationDAO.get());
      PlacesDTO placesDTO = placesDAOToDTO(placesDAO.get());
      locationDTO.setPlacesDTOList(Arrays.asList(placesDTO));
      return locationDTO;
    } else {
      // Handle the case when the location is not found
      // For example, you can throw an exception or return an appropriate response.
      // Here, we return null for simplicity.
      return null;
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
  public Optional<LocationDAO> getLocationById(Integer zipcode) {

    return Optional.ofNullable(locationRepository.findById(zipcode).orElse(null));
  }
  // method to get places data from repo
  public Optional<PlacesDAO> getPlacesById(Integer zipcode) {

    return Optional.ofNullable(placesRepository.findById(zipcode).orElse(null));
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

  public void setModelMapper(ModelMapper modelMapperMock) {
    this.modelMapper=modelMapperMock;
  }
}
