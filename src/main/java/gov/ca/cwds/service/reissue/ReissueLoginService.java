package gov.ca.cwds.service.reissue;

import org.springframework.security.oauth2.client.OAuth2ClientContext;

/**
 * Created by TPT2 on 10/24/2017.
 */
public interface ReissueLoginService {
  String issueAccessCode(String providerId, OAuth2ClientContext oauth2ClientContext);

  String validate(String perryToken);

  void invalidate(String perryToken);

  String issueToken(String accessCode);
}
