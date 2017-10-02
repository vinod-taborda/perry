package gov.ca.cwds.config;

import gov.ca.cwds.PerryProperties;
import gov.ca.cwds.rest.api.domain.PerryException;
import gov.ca.cwds.service.WhiteList;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dmitry.rudenko on 10/2/2017.
 */
public class TestLoginServiceValidatorFilter {
  @Test
  public void testLoginUrlMatchesAndValidCallback() throws IOException, ServletException {
    LoginServiceValidatorFilter validatorFilter = new LoginServiceValidatorFilter();
    RequestMatcher requestMatcher = Mockito.mock(RequestMatcher.class);
    validatorFilter.setRequestMatcher(requestMatcher);
    HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
    Mockito.when(requestMatcher.matches(httpServletRequest)).thenReturn(true);
    Mockito.when(httpServletRequest.getRequestURI()).thenReturn("requestUrl");
    Mockito.when(httpServletRequest.getParameter("callback")).thenReturn("callbackUrl");
    WhiteList whiteList = new WhiteList();
    PerryProperties perryProperties = new PerryProperties();
    perryProperties.setWhiteList("callbackUrl");
    whiteList.setConfiguration(perryProperties);
    validatorFilter.setWhiteList(whiteList);
    validatorFilter.doFilter(httpServletRequest, httpServletResponse, Mockito.mock(FilterChain.class));
  }

  @Test(expected = PerryException.class)
  public void testLoginUrlMatchesAndInvalidCallback() throws IOException, ServletException {
    LoginServiceValidatorFilter validatorFilter = new LoginServiceValidatorFilter();
    RequestMatcher requestMatcher = Mockito.mock(RequestMatcher.class);
    validatorFilter.setRequestMatcher(requestMatcher);
    HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
    Mockito.when(requestMatcher.matches(httpServletRequest)).thenReturn(true);
    Mockito.when(httpServletRequest.getRequestURI()).thenReturn("requestUrl");
    Mockito.when(httpServletRequest.getParameter("callback")).thenReturn("invalidCallbackUrl");
    WhiteList whiteList = new WhiteList();
    PerryProperties perryProperties = new PerryProperties();
    perryProperties.setWhiteList("callbackUrl");
    whiteList.setConfiguration(perryProperties);
    validatorFilter.setWhiteList(whiteList);
    validatorFilter.doFilter(httpServletRequest, httpServletResponse, Mockito.mock(FilterChain.class));
  }

  @Test
  public void testLoginUrlNotMatchesAndInvalidCallback() throws IOException, ServletException {
    LoginServiceValidatorFilter validatorFilter = new LoginServiceValidatorFilter();
    RequestMatcher requestMatcher = Mockito.mock(RequestMatcher.class);
    validatorFilter.setRequestMatcher(requestMatcher);
    HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse httpServletResponse = Mockito.mock(HttpServletResponse.class);
    Mockito.when(requestMatcher.matches(httpServletRequest)).thenReturn(false);
    Mockito.when(httpServletRequest.getRequestURI()).thenReturn("requestUrl");
    Mockito.when(httpServletRequest.getParameter("callback")).thenReturn("invalidCallbackUrl");
    WhiteList whiteList = new WhiteList();
    PerryProperties perryProperties = new PerryProperties();
    perryProperties.setWhiteList("callbackUrl");
    whiteList.setConfiguration(perryProperties);
    validatorFilter.setWhiteList(whiteList);
    validatorFilter.doFilter(httpServletRequest, httpServletResponse, Mockito.mock(FilterChain.class));
  }


}


