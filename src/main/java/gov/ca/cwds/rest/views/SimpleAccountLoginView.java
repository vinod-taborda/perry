package gov.ca.cwds.rest.views;

import gov.ca.cwds.rest.SimpleAccountLoginConfiguration;
import io.dropwizard.views.View;

public class SimpleAccountLoginView extends View {
  private SimpleAccountLoginConfiguration simpleAccountLoginConfiguration;

  private String message;

  private String callback;

  public SimpleAccountLoginView(SimpleAccountLoginConfiguration simpleAccountLoginConfiguration,
      String message, String callback) {
    super(simpleAccountLoginConfiguration.getTemplateName());
    this.simpleAccountLoginConfiguration = simpleAccountLoginConfiguration;
    this.message = message;
    this.callback = callback;
  }

  /**
   * @return the simpleAccountLoginConfiguration
   */
  public SimpleAccountLoginConfiguration getSimpleAccountLoginConfiguration() {
    return simpleAccountLoginConfiguration;
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @return the callback
   */
  public String getCallback() {
    return callback;
  }



}
