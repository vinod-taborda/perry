package gov.ca.cwds.security.module;

import gov.ca.cwds.security.annotations.Authorize;
import gov.ca.cwds.security.configuration.SecurityConfiguration;
import gov.ca.cwds.security.permission.AbacPermission;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;

/**
 * Created by dmitry.rudenko on 9/25/2017.
 */
public class AbacMethodInterceptor implements MethodInterceptor {

  private ScriptEngine scriptEngine;
  private Boolean enabled;

  public AbacMethodInterceptor() {
    scriptEngine = new ScriptEngineManager().getEngineByName("groovy");
  }

  @Override
  public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    if (!isEnabled()) {
      return methodInvocation.proceed();
    }

    checkParametersPermissions(methodInvocation);
    Object result = methodInvocation.proceed();
    result = checkResultPermissions(result, methodInvocation);
    return result;
  }

  private Object checkResultPermissions(Object result, MethodInvocation methodInvocation) throws ScriptException{
    Authorize authorize = methodInvocation.getMethod().getAnnotation(Authorize.class);
    if (authorize == null) {
      return result;
    }
    if (result instanceof Collection) {
      return filterResult(authorize, (Collection) result);
    } else {
      checkPermissions(authorize, result);
      return result;
    }
  }

  private void checkParametersPermissions(MethodInvocation methodInvocation) throws ScriptException {
    Parameter[] parameters = methodInvocation.getMethod().getParameters();
    for (int i = 0; i < parameters.length; i++) {
      Authorize authorize = parameters[i].getAnnotation(Authorize.class);
      if (authorize != null) {
        Object arg = methodInvocation.getArguments()[i];
        checkPermissions(authorize, arg);
      }
    }
  }

  private void checkPermissions(Authorize authorize, Object object) throws ScriptException {
    for (String permission : authorize.value()) {
      checkPermission(permission, object);
    }
  }

  @SuppressWarnings("unchecked")
  private void checkPermission(String permission, Object arg) throws ScriptException {
    AbacPermission abacPermission = new AbacPermission(permission);
    String selector = abacPermission.getSecuredObject().toString();
    int dotIndex = selector.indexOf('.');
    String identifier;
    if (dotIndex == -1) {
      identifier = selector;
    } else {
      identifier = selector.substring(0, dotIndex);
    }
    ScriptContext scriptContext = new SimpleScriptContext();
    scriptContext.setAttribute(identifier, arg, ScriptContext.ENGINE_SCOPE);
    for (Object o : (Collection<Object>) scriptEngine.eval("[" + selector + "].flatten()", scriptContext)) {
      abacPermission.setSecuredObject(o);
      SecurityUtils.getSubject().checkPermission(abacPermission);
    }
  }

  @SuppressWarnings("unchecked")
  private Collection filterResult(Authorize authorize, Collection results) throws ScriptException {
    Collection out = initOutput(results);
    for (Object result : results) {
      try {
        checkPermissions(authorize, result);
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

  private boolean isEnabled() {
    if (enabled == null) {
      final SecurityConfiguration securityConfiguration = getSecurityConfigurationFromGuice();
      final Boolean authorization = securityConfiguration.getAuthorization();
      enabled = authorization == null || authorization;
    }
    return enabled;

  }

  private SecurityConfiguration getSecurityConfigurationFromGuice() {
    try {
      return SecurityModule.injector().getInstance(SecurityConfiguration.class);
    } catch (Exception e) {
      return new SecurityConfiguration();
    }
  }
}
