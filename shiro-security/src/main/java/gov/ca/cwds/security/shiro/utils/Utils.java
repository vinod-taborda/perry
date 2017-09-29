package gov.ca.cwds.security.shiro.utils;

/**
 * Created by dmitry.rudenko on 9/29/2017.
 */
public class Utils {
  public static String replaceCRLF(String string) {
    return string.replaceAll("[\\n\\r]", " ");
  }
}
