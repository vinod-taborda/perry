package gov.ca.cwds.security.test;

import gov.ca.cwds.security.realm.PerryAccount;
import gov.ca.cwds.utils.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author CWDS CALS API Team
 */

public class TestSecurityFilter extends AuthenticatingFilter {

    public static final String PATH_TO_PRINCIPAL_FIXTURE = "pathToPrincipalFixture";
    public static final String PATH_TO_DEFAULT_PRINCIPAL_FIXTURE = "default-principal.json";

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        AuthenticationToken token = new AuthenticationToken() {
            @Override
            public Object getPrincipal() {
                try {
                    String pathToPrincipalFixture = request.getParameter(PATH_TO_PRINCIPAL_FIXTURE);
                    if (StringUtils.isEmpty(pathToPrincipalFixture)) {
                        pathToPrincipalFixture = PATH_TO_DEFAULT_PRINCIPAL_FIXTURE;
                    }
                    try (InputStream principalJson = getClass().getClassLoader().getResourceAsStream(pathToPrincipalFixture)) {
                      return JsonUtils.from(IOUtils.toString(principalJson, "UTF-8"), PerryAccount.class);
                    }
                } catch (IOException e) {
                    throw new IllegalStateException("Can't get principal", e);
                }
            }

            @Override
            public Object getCredentials() {
                return new Object();
            }
        };
        return token;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        return executeLogin(request, response);
    }

}
