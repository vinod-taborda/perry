package gov.ca.cwds.security.module;

import gov.ca.cwds.security.annotations.Authorize;
import gov.ca.cwds.security.permission.PermissionString;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;

import javax.script.*;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmitry.rudenko on 9/25/2017.
 */
public class AbacMethodInterceptor implements MethodInterceptor {
  private ScriptEngine scriptEngine;

  public AbacMethodInterceptor() {
    scriptEngine = new ScriptEngineManager().getEngineByName("groovy");
  }

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    String[] permissions = methodInvocation.getMethod().getAnnotation(Authorize.class).value();
    Set<PermissionString> resultPermissions = new HashSet<>();
    for (String permission : permissions) {
      PermissionString permissionString = new PermissionString(permission);
      if (permissionString.isTemplate()) {
        if (!permissionString.isResultPermission()) {
          checkArgPermission(permissionString, methodInvocation);
        } else {
          resultPermissions.add(permissionString);
        }
      } else {
        SecurityUtils.getSubject().checkPermission(permission);
      }
    }

    Object result = methodInvocation.proceed();
    if (resultPermissions.isEmpty()) {
      return result;
    }
    if (result instanceof Collection) {
      return filterResult(resultPermissions, (Collection) result);
    } else {
      checkResultPermissions(resultPermissions, result);
      return result;
    }
  }

  private void checkArgPermission(PermissionString permissionString, MethodInvocation methodInvocation) throws Exception {
    ScriptContext scriptContext = scriptContextFromArgs(methodInvocation);
    checkIds(permissionString, scriptContext);
  }

  private void checkIds(PermissionString permissionString, ScriptContext scriptContext) {
    Collection<Object> ids = selectIds(permissionString.getSelector(), scriptContext);
    ids.stream()
            .map(permissionString::apply)
            .forEach(permission -> SecurityUtils.getSubject().checkPermission(permission));
  }

  private ScriptContext scriptContextFromArgs(MethodInvocation methodInvocation) {
    ScriptContext scriptContext = new SimpleScriptContext();
    for (int i = 0; i < methodInvocation.getMethod().getParameterCount(); i++) {
      Parameter parameter = methodInvocation.getMethod().getParameters()[i];
      scriptContext.setAttribute(parameter.getName(), methodInvocation.getArguments()[i], ScriptContext.ENGINE_SCOPE);
    }
    return scriptContext;
  }

  @SuppressWarnings("unchecked")
  private Collection<Object> selectIds(String selector, ScriptContext scriptContext) {
    try {
      return (Collection<Object>) scriptEngine.eval("[" + selector + "].flatten()", scriptContext);
    } catch (ScriptException e) {
      throw new AuthorizationException(e);
    }
  }

  private void checkResultPermissions(Set<PermissionString> permissions, Object result)  {
    permissions.forEach(permission -> checkResultPermission(permission, result));
  }

  private void checkResultPermission(PermissionString permission, Object result) {
    ScriptContext scriptContext = scriptContextFromResult(result);
    checkIds(permission, scriptContext);
  }

  private ScriptContext scriptContextFromResult(Object result) {
    ScriptContext scriptContext = new SimpleScriptContext();
    scriptContext.setAttribute(PermissionString.RESULT_KEYWORD, result, ScriptContext.ENGINE_SCOPE);
    return scriptContext;
  }

  @SuppressWarnings("unchecked")
  private Collection filterResult(Set<PermissionString> permissions, Collection results) throws IllegalAccessException, InstantiationException, ScriptException {
    Collection out = initOutput(results);
    for (Object result : results) {
      try {
        checkResultPermissions(permissions, result);
        out.add(result);
      } catch (AuthorizationException e) {
        //ignore
      }
    }
    return out;
  }

  private Collection initOutput(Collection results) {
    if (results instanceof Set) {
      return new HashSet(results.size());
    } else
      return new ArrayList(results.size());
  }
}
