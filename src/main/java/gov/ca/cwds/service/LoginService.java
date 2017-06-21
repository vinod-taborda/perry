package gov.ca.cwds.service;

/**
 * Created by dmitry.rudenko on 5/23/2017.
 */
public interface LoginService {
    String login(String providerId) throws Exception;

    String validate(String token) throws Exception;
}
