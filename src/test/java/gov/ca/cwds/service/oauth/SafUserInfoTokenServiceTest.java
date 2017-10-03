package gov.ca.cwds.service.oauth;

import gov.ca.cwds.service.SAFService;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.HashMap;

/**
 * Created by dmitry.rudenko on 10/3/2017.
 */
public class SafUserInfoTokenServiceTest {
  @Test
  public void testLoadAuthentication() {
    PrincipalExtractor principalExtractor = Mockito.mock(PrincipalExtractor.class);
    SAFService safService = Mockito.mock(SAFService.class);
    SafUserInfoTokenService safUserInfoTokenService = new SafUserInfoTokenService(safService,
            "userInfoEndpointUrl", "clientId");
    safUserInfoTokenService.setPrincipalExtractor(principalExtractor);

    Mockito.when(principalExtractor.extractPrincipal(new HashMap<>())).thenReturn("principal");

    OAuth2Authentication authentication = safUserInfoTokenService.loadAuthentication("token");
    System.out.println(authentication);
    assert authentication.getPrincipal().equals("principal");
    assert authentication.getUserAuthentication() instanceof UsernamePasswordAuthenticationToken;
    assert authentication.getUserAuthentication().getPrincipal().equals("principal");
    assert authentication.getUserAuthentication().getAuthorities().size() == 1;
    assert authentication.getUserAuthentication().getAuthorities().iterator().next().getAuthority().equals("ROLE_USER");
    assert authentication.getUserAuthentication().getDetails().equals(new HashMap<>());
    assert authentication.getUserAuthentication().getCredentials().equals("N/A");


  }
}
