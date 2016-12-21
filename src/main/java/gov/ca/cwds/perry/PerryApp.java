package gov.ca.cwds.perry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PerryApp {

  @Bean
  public Client getJerseyClient() {
    Client client = ClientBuilder.newClient(new ClientConfig());
    client.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "FINEST");
    return client;
  }

  public static void main(String[] args) {
    SpringApplication.run(PerryApp.class, args);
  }
}

