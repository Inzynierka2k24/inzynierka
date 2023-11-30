package com.inzynierka2k24.apiserver;

import com.inzynierka2k24.apiserver.grpc.integration.ExternalIntegrationServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication()
@ConfigurationPropertiesScan(basePackageClasses = ExternalIntegrationServiceProperties.class)
public class ApartManagerApiServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApartManagerApiServerApplication.class, args);
  }
}
