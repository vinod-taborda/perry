package gov.ca.cwds.smoketest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by dmitry.rudenko on 8/24/2017.
 */
public class SmokeTest {


  @Test
  public void test() throws Exception {
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper mapper = new ObjectMapper();
    String result = restTemplate.getForObject(System.getProperty("perry.health.check.url"), String.class);
    Map map = mapper.readValue(result, Map.class);
    if ("DOWN".equals(map.get("status"))) {
      throw new Exception(result);
    }
  }
}
