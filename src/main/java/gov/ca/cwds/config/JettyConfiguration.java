package gov.ca.cwds.config;

import org.eclipse.jetty.server.ServerConnector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dmitry.rudenko on 6/23/2017.
 */
@Configuration
public class JettyConfiguration {
  @Value("${server.http.port}")
  private int httpPort;

  @Bean
  public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
    return new EmbeddedServletContainerCustomizer() {

      @Override
      public void customize(ConfigurableEmbeddedServletContainer container) {
        if (container instanceof JettyEmbeddedServletContainerFactory) {
          customizeJetty((JettyEmbeddedServletContainerFactory) container);
        }
      }

      private void customizeJetty(JettyEmbeddedServletContainerFactory container) {

        container.addServerCustomizers((JettyServerCustomizer) server -> {
          // HTTP
          ServerConnector connector = new ServerConnector(server);
          connector.setPort(httpPort);
          server.addConnector(connector);

        });
      }
    };
  }
}
