package gov.ca.cwds.service;

import gov.ca.cwds.PerryConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by dmitry.rudenko on 5/22/2017.
 */
@Service
public class SAFService {
    private static final String BEARER = "bearer ";
    @Autowired
    private RestTemplate client;
    @Autowired
    private OAuth2ClientContext oauth2ClientContext;
    @Autowired
    private PerryConfiguration perryConfiguration;

    public String getUserInfo() {
        //TODO
        if (true) {
            return "JAMESSC";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String bearer = BEARER + oauth2ClientContext.getAccessToken().getValue();
        headers.add(HttpHeaders.AUTHORIZATION, bearer);
        return client.postForObject(perryConfiguration.getSaf().getUserInfoUrl(), new HttpEntity<>(bearer, headers), String.class);
    }

}
