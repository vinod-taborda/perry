package gov.ca.cwds.service;

import gov.ca.cwds.PerryProperties;
import gov.ca.cwds.rest.api.domain.PerryException;
import org.junit.Test;

/**
 * Created by dmitry.rudenko on 10/2/2017.
 */
public class WhiteListTest {

  @Test(expected = PerryException.class)
  public void testEnabledInvalidUrl() {
    WhiteList whiteList = new WhiteList();
    PerryProperties perryProperties = new PerryProperties();
    perryProperties.setWhiteList("url1 url2");
    whiteList.configuration = perryProperties;
    whiteList.validate("", "url3");
  }

  @Test
  public void testEnabledValidUrl() {
    WhiteList whiteList = new WhiteList();
    PerryProperties perryProperties = new PerryProperties();
    perryProperties.setWhiteList("url1 url2");
    whiteList.configuration = perryProperties;
    whiteList.validate("", "url1");
  }

  @Test
  public void testDisabled() {
    WhiteList whiteList = new WhiteList();
    PerryProperties perryProperties = new PerryProperties();
    perryProperties.setWhiteList("*");
    whiteList.configuration = perryProperties;
    whiteList.validate("", "url3");
  }
}
