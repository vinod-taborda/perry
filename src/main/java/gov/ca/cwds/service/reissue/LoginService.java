package gov.ca.cwds.service.reissue;

/**
 * Created by TPT2 on 10/24/2017.
 */
public interface LoginService {
  String issueAccessCode(String providerId);

  String validate(String perryToken);

  void invalidate(String perryToken);

  String issueToken(String accessCode);
}
