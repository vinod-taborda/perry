package gov.ca.cwds.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author CWDS CALS API Team
 */

public final class JsonUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private JsonUtils() {}

  public static String to(Object o) throws JsonProcessingException {
    return objectMapper.writeValueAsString(o);
  }

  public static <T> T from(String json, Class<T> clazz) throws IOException {
    return objectMapper.readValue(json, clazz);
  }
}
