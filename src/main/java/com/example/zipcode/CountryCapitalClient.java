package com.example.zipcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class CountryCapitalClient extends WebServiceGatewaySupport {
  private static final Logger log = LoggerFactory.getLogger(CountryCapitalClient.class);

  public CapitalCityResponse getCountryCapital(String country) {
    CapitalCity request = new CapitalCity();
    request.setSCountryISOCode(country);
    log.info("Requesting capital city for :" + country);
    CapitalCityResponse response =
        (CapitalCityResponse)
            getWebServiceTemplate()
                .marshalSendAndReceive(
                    "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso",
                    request);
    return response;
  }
}
