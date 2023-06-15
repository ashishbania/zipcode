package com.example.zipcode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class CapitalConfiguration {
  @Bean
  public Jaxb2Marshaller marshaller() {
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    // this package must match the package in the <generatePackage> specified in
    // pom.xml
    marshaller.setContextPath("com.example.zipcode");
    return marshaller;
  }

  @Bean
  public CountryCapitalClient countryClient(Jaxb2Marshaller marshaller) {
    CountryCapitalClient client = new CountryCapitalClient();
    client.setDefaultUri(
        "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso");
    client.setMarshaller(marshaller);
    client.setUnmarshaller(marshaller);
    return client;
  }
}
