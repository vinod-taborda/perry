package gov.ca.cwds;

import gov.ca.cwds.security.jwt.JwtService;
import gov.ca.cwds.smoketest.JwtCheck;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

/**
 * Created by dmitry.rudenko on 10/2/2017.
 */
public class HealthCheckTest {
  @Test
  public void testUp() {
    JwtService jwtService = Mockito.mock(JwtService.class);
    Mockito.when(jwtService.generate("id", "subject", "identity")).thenReturn("token");
    Mockito.when(jwtService.validate("token")).thenReturn("identity");
    JwtCheck jwtCheck = new JwtCheck();
    jwtCheck.setJwtService(jwtService);
    Health health = jwtCheck.health();
    assert health.getStatus().equals(Status.UP);

  }

  @Test
  public void testDown() {
    JwtService jwtService = Mockito.mock(JwtService.class);
    Mockito.when(jwtService.generate("id", "subject", "identity")).thenReturn("token");
    Mockito.when(jwtService.validate("token")).thenReturn("invalidIdentity");
    JwtCheck jwtCheck = new JwtCheck();
    jwtCheck.setJwtService(jwtService);
    Health health = jwtCheck.health();
    assert health.getStatus().equals(Status.DOWN);

  }
}
