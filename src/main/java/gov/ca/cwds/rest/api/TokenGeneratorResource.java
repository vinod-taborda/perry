package gov.ca.cwds.rest.api;

import gov.ca.cwds.service.TokenGeneratorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.ws.rs.POST;

/**
 * Created by dmitry.rudenko on 6/15/2017.
 */
@RestController
@Profile("dev")
public class TokenGeneratorResource {
  @Autowired
  TokenGeneratorService tokenGeneratorService;

  @POST
  @RequestMapping("/dev/generate")
  @Transactional
  @ApiOperation(value = "Generate Token")
  public String generate(@ApiParam(required = true, name = "identity",
          value = "Jwt identity.", example = "{\"user\":\"username\", \"roles\":[\"role1\",\"role2\"], \"email\":\"test@email.com\"}")
                         @RequestParam("identity") String identity) throws Exception {
    return tokenGeneratorService.generate(identity);
  }
}
