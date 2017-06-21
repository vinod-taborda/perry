package gov.ca.cwds.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by dmitry.rudenko on 5/23/2017.
 */
@Profile("dev")
@Service
public class FormLoginService implements LoginService {
    @Autowired
    private TokenGeneratorService tokenGeneratorService;
    @Autowired
    private JwtService jwtService;

    @Override
    @SuppressWarnings("unchecked")
    public String login(String providerId) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String username = authenticationToken.getName();
        return tokenGeneratorService.generate(username);
    }

    @Override
    public String validate(String token) throws Exception {
        return jwtService.validateToken(token);
    }
}
