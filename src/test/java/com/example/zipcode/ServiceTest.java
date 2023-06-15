package com.example.zipcode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

  LocationDAO locationDAO;
  LocationDTO locationDTO;
  @Mock private LocationRepository locationRepository;
  @Mock private PlacesRepository placesRepository;
  private ModelMapper modelMapper;
  @InjectMocks private Service service;

  @BeforeEach
  void setUp() {
    System.out.println("Setting up method for the service layer");
    modelMapper = new ModelMapper(); // Instantiate the ModelMapper object
    locationDAO = new LocationDAO(28031, "UNITED STATES", "US");
    locationDTO = new LocationDTO(28031, "United States", "US");
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
    Integer mockZipCode = 0;
    // when
    when(locationRepository.findById(mockZipCode)).thenReturn(Optional.of(new LocationDAO()));
    when(placesRepository.findById(mockZipCode)).thenReturn(Optional.of(new PlacesDAO()));

    Boolean returnedValue = service.isDataPresent(mockZipCode);
    // then
    verify(locationRepository, times(1)).findById(mockZipCode);
    verify(placesRepository, times(1)).findById(mockZipCode);
    assertEquals(returnedValue, true);
  }

  @Test
  void testGetZipcodeData() {
    //
    //    // Mocking the location repository findById method
    //    Integer dummyZipcode = 28031;
    //    LocationDTO locationDTO = new LocationDTO(dummyZipcode, "United States", "US",
    // "Washington", (List<PlacesDTO>) new PlacesDTO("Cornelius","-80.8726","North Carolina","NC",
    // "35.4733"));
    //    when(locationRepository.findById(dummyZipcode)).thenReturn((Optional<LocationDAO>)
    // Optional.of(locationDTO));
    //
    //    //calling the getZipCode method
    //
    //    LocationDAO result = service.getZipcodeData(dummyZipcode);
  }

  @Test
  @Disabled
  void testLocationDTOToDAO1() {
    // Mocking the model mapper method
    // given
    // Mocking the location repo save method
    LocationDAO expected = new LocationDAO();
    //    when(modelMapper.map(locationDTO, LocationDAO.class)).thenReturn(expected);
    when(modelMapper.map(Mockito.any(LocationDTO.class), Mockito.eq(LocationDAO.class)))
        .thenReturn(new LocationDAO());

    //    LocationDAO result = service.locationDTOToDAO(locationDTO);

    // verify
    //    verify(modelMapper, times(1)).map(locationDAO, LocationDTO.class);
    //    assertEquals(expected, result);
    LocationDAO result = service.locationDTOToDAO(locationDTO);

    // Verify the method call on the modelMapper mock object
    verify(modelMapper, times(1))
        .map(Mockito.any(LocationDTO.class), Mockito.eq(LocationDAO.class));
    //    assertNotNull(result);
  }

  @Test
  @Disabled
  void testLocationDTOToDAO() {
    // Stub the method call on the modelMapper mock object
    when(modelMapper.map(Mockito.any(LocationDTO.class), Mockito.eq(LocationDAO.class)))
        .thenAnswer(
            invocation -> {
              LocationDTO source = invocation.getArgument(0);
              LocationDAO destination = new LocationDAO();
              // Perform mapping logic here, e.g., set values on the destination object
              destination.setZipCode(source.getZipCode());
              destination.setCountry(source.getCountry());
              //              destination.setState(source.getState());
              return destination;
            });

    LocationDAO result = service.locationDTOToDAO(locationDTO);

    // Verify the method call on the modelMapper mock object
    verify(modelMapper, times(1))
        .map(Mockito.any(LocationDTO.class), Mockito.eq(LocationDAO.class));
    assertNotNull(result);
  }

  @Test
  @Disabled
  void locationDTOToDAO() {}
}
