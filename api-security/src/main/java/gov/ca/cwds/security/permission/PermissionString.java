package gov.ca.cwds.security.permission;

/**
 * Created by dmitry.rudenko on 9/27/2017.
 */
public class PermissionString {
  private static final String PARAM_CHAR = "&";
  public static final String RESULT_KEYWORD = "result";
  private String staticPart;
  private String selector;
  private boolean resultPermission;
  private boolean template;

  public PermissionString(String permission) {
    int indexOfParam = permission.indexOf(PARAM_CHAR);
    template = indexOfParam != -1;
    if(template) {
      selector = permission.substring(indexOfParam + 1);
      staticPart = permission.substring(0, indexOfParam);
      resultPermission = permission.contains(PARAM_CHAR + RESULT_KEYWORD);
    }
    else {
      staticPart = permission;
    }
  }

  public String getStaticPart() {
    return staticPart;
  }

  public String getSelector() {
    return selector;
  }

  public boolean isResultPermission() {
    return resultPermission;
  }

  public boolean isTemplate() {
    return template;
  }

  public String apply(Object id) {
    if(template) {
      return staticPart + id.toString();
    }
    else {
      return staticPart;
    }
  }
}
