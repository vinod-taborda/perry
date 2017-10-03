package gov.ca.cwds.service;

/**
 * @author CWDS TPT-2 Team
 */
public interface TokenService {
  void invalidate(String token);
  String validate(String token);
}
