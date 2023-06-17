package com.example.zipcode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

  LocationDAO locationDAO;
  LocationDTO locationDTO;
  PlacesDAO placesDAO;
  PlacesDTO placesDTO;
  @Mock LocationRepository locationRepository;
  @Mock PlacesRepository placesRepository;
  @InjectMocks Service service;
  @Mock ModelMapper modelMapper;
  @Mock CountryCapitalClient client;

  @BeforeEach
  void setUp() {
    //    System.out.println("Setting up method for the service layer");
    modelMapper = new ModelMapper(); // Instantiate the ModelMapper object
    locationDAO = new LocationDAO(28031, "UNITED STATES", "US");
    locationDTO = new LocationDTO(28031, "United States", "US");
    placesDAO = new PlacesDAO(28031, "Cornelius", "-123.45", "North Carolina", "NC", "-78952");
    placesDTO = new PlacesDTO("Cornelius", "-80.896", "North Carolina", "NC", "35047");
    this.service =
        new Service(this.locationRepository, this.placesRepository, this.modelMapper, this.client);
  }

  @AfterEach
  void tearDown() {
    System.out.println("Tearing down method for the service layer");
  }

  @Test
  void testPersistPlacesData() {
    // Mocking the places repo save method
    PlacesDAO placesDAO = new PlacesDAO();
    PlacesDAO placesDAO1 =
        new PlacesDAO(28031, "Cornelius", "-123", "North Carolina", "NC", "-456");
    when(placesRepository.save(Mockito.any(PlacesDAO.class))).thenReturn(placesDAO1);
    service.persistPlacesData(placesDAO1);
    // verify
    verify(placesRepository, times(1)).save(Mockito.any(PlacesDAO.class));
  }

  @Test
  void testPersistLocationData() {
    // Mocking the location repo save method
    //    LocationDAO locationDAO = new LocationDAO();
    when(locationRepository.save(Mockito.any(LocationDAO.class))).thenReturn(locationDAO);
    service.persistLocationData(locationDAO);
    // verify
    verify(locationRepository, times(1)).save(Mockito.any(LocationDAO.class));
  }

  @Test
  void testIsDataPresentYes() {
    // Mocking the location repo findById method
    // given
    Integer mockZipCode = 28031;
    // when
    when(locationRepository.findById(mockZipCode)).thenReturn(Optional.of(new LocationDAO()));
    when(placesRepository.findById(mockZipCode)).thenReturn(Optional.of(new PlacesDAO()));

    service.isDataPresent(mockZipCode);
    // then
    verify(locationRepository, times(1)).findById(mockZipCode);
    verify(placesRepository, times(1)).findById(mockZipCode);
  }

  @Test
  void testIsDataPresentNo() {
    // Mocking the location repo findById method
    // given
    Integer testData = 0;
    // when
    when(locationRepository.findById(testData)).thenReturn(Optional.empty());
    //    when(placesRepository.findById(testData)).thenReturn(Optional.empty());

    service.isDataPresent(testData);
    // then
    verify(locationRepository, times(1)).findById(testData);
    //    verify(placesRepository, times(1)).findById(testData);
    //    assertEquals(returnedValue, true);
  }

  @Test
  void testGetZipcodeData() {
    Integer dummyZipcode = 28031;

    LocationDTO locationDTO =
        new LocationDTO(dummyZipcode, "United States", "US", "Washington", List.of(placesDTO));
    when(locationRepository.findById(dummyZipcode)).thenReturn((Optional.ofNullable(locationDAO)));
    when(client.getCountryCapital("US")).thenReturn(new CapitalCityResponse());
    //    when(service.getLocationDataFromDB(dummyZipcode)).thenReturn(locationDTO);
    service.getZipcodeData(dummyZipcode);
    verify(locationRepository, times(1)).findById(dummyZipcode);
  }
  //    when(service.getLocationDataFromDB(dummyZipcode)).thenReturn(locationDTO);
  //    when(locationRepository.findById(dummyZipcode)).thenReturn(Optional.of(locationDAO));
  //    service.getZipcodeData(dummyZipcode);
  //    verify(service, times(1)).getLocationDataFromDB(dummyZipcode);
  //     Optional.of(locationDTO));
  //
  //    //calling the getZipCode method
  //
  //    LocationDAO result = service.getZipcodeData(dummyZipcode);

  @Test
  void testGetLocationDataFromDB() {
    // Mocking the get location and get places methods
    Integer mockZipCode = 28031;
    LocationDAO locationDAO = new LocationDAO(28031, "United States", "US");
    PlacesDAO placesDAO =
        new PlacesDAO(28031, "Cornelius", "-123.45", "North Carolina", "NC", "-78952");
    // when
    //    when(service.getLocationById(mockZipCode)).thenReturn(Optional.of(locationDAO));
    //    when(service.getPlacesById(mockZipCode)).thenReturn(Optional.of(placesDAO));
    System.out.println("before when");
    when(locationRepository.findById(mockZipCode)).thenReturn(Optional.ofNullable(locationDAO));
    when(placesRepository.findById(mockZipCode)).thenReturn(Optional.ofNullable(placesDAO));
    System.out.println("after when");
    LocationDTO returnedValue = service.getLocationDataFromDB(mockZipCode);
    System.out.println("after service call");
    // then
    verify(locationRepository, times(1)).findById(mockZipCode);
    verify(placesRepository, times(1)).findById(mockZipCode);
    System.out.println("after verify call");
    //  assertEquals(returnedValue, true);
  }

  @Test
  void testGetLocationDataFromDB_returnNull() {
    // Mocking the get location and get places methods
    Integer mockZipCode = 28031;
    LocationDAO locationDAO = new LocationDAO(28031, "United States", "US");
    PlacesDAO placesDAO =
        new PlacesDAO(28031, "Cornelius", "-123.45", "North Carolina", "NC", "-78952");
    when(locationRepository.findById(mockZipCode)).thenReturn(Optional.empty());
    when(placesRepository.findById(mockZipCode)).thenReturn(Optional.empty());
    LocationDTO returnedValue = service.getLocationDataFromDB(mockZipCode);
    System.out.println("after service call");
    // then
    verify(locationRepository, times(1)).findById(mockZipCode);
    verify(placesRepository, times(1)).findById(mockZipCode);
    System.out.println("after verify call");
    //  assertEquals(returnedValue, true);
  }

  @Test
  void testGetLocationDataFromAPI() {
    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    //    CountryCapitalClient client = Mockito.mock(CountryCapitalClient.class);
    Integer zipcode = 28031;
    //    when(restTemplate.getForObject("https://api.zippopotam.us/us/" + zipcode,
    // LocationDTO.class))
    //        .thenReturn(locationDTO);
    when(client.getCountryCapital("US")).thenReturn(new CapitalCityResponse());
    LocationDTO result = service.getLocationDataFromAPI(28031);
    LocationDTO expected = result;
    assertEquals(result, expected);
    //    verify(restTemplate, times(1)).getForObject("uri", LocationDTO.class);
  }

  @Test
  void testLocationDTOToDAO1() {
    // Mocking the model mapper method
    // given
    // Mocking the location repo save method
    LocationDAO expected = new LocationDAO();
    locationDAO = new LocationDAO(28031, "UNITED STATES", "US");
    locationDTO = new LocationDTO(28031, "United States", "US");
    // Mocking the modelMapper bean
    ModelMapper modelMapperMock = Mockito.mock(ModelMapper.class);
    when(modelMapperMock.map(locationDTO, LocationDAO.class)).thenReturn(locationDAO);
    service.setModelMapper(modelMapperMock); // Set the mock modelMapper in the service

    LocationDAO result = service.locationDTOToDAO(locationDTO);
    // verify
    verify(modelMapperMock, times(1)).map(locationDTO, LocationDAO.class);
  }

  @Test
  void testLocationDAOToDTO() {

    // Mocking the model mapper method
    // given
    // Mocking the location repo save method
    LocationDTO expected = new LocationDTO();
    locationDAO = new LocationDAO(28031, "UNITED STATES", "US");
    locationDTO = new LocationDTO(28031, "United States", "US");
    // Mocking the modelMapper bean
    ModelMapper modelMapperMock = Mockito.mock(ModelMapper.class);
    when(modelMapperMock.map(locationDAO, LocationDTO.class)).thenReturn(locationDTO);
    service.setModelMapper(modelMapperMock); // Set the mock modelMapper in the service

    LocationDTO result = service.locationDAOToDTO(locationDAO);
    // verify
    verify(modelMapperMock, times(1)).map(locationDAO, LocationDTO.class);
  }

  @Test
  void testPlacesDAOToDTO() {
    PlacesDTO expected = new PlacesDTO();
    ModelMapper modelMapperMock = Mockito.mock(ModelMapper.class);
    when(modelMapperMock.map(placesDAO, PlacesDTO.class)).thenReturn(placesDTO);
    service.setModelMapper(modelMapperMock); // Set the mock modelMapper in the service
    PlacesDTO result = service.placesDAOToDTO(placesDAO);
    verify(modelMapperMock, times(1)).map(placesDAO, PlacesDTO.class);
  }

  @Test
  void testPlacesDTOToDAO() {

    // Mocking the model mapper method
    // given
    // Mocking the location repo save method
    //    LocationDTO expected = new LocationDTO();
    //    locationDAO = new LocationDAO(28031, "UNITED STATES", "US");
    //    locationDTO = new LocationDTO(28031, "United States", "US");
    // Mocking the modelMapper bean
    ModelMapper modelMapperMock = Mockito.mock(ModelMapper.class);
    when(modelMapperMock.map(placesDTO, PlacesDAO.class)).thenReturn(placesDAO);
    service.setModelMapper(modelMapperMock); // Set the mock modelMapper in the service

    PlacesDAO result = service.placesDTOToDAO(placesDTO, 28031);
    // verify
    verify(modelMapperMock, times(1)).map(placesDTO, PlacesDAO.class);
  }

  @Test
  void testGetLocationByIdReturnsLocationDAO() {
    when(locationRepository.findById(123)).thenReturn(Optional.ofNullable(locationDAO));
    service.getLocationById(123);
    verify(locationRepository, times(1)).findById(123);
  }

  @Test
  void testGetLocationByIdReturnsNull() {
    when(locationRepository.findById(null)).thenReturn(Optional.empty());
    service.getLocationById(null);
    verify(locationRepository, times(1)).findById(null);
  }
}
