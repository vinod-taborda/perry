package gov.ca.cwds.security.authorizer;

import org.apache.shiro.authz.AuthorizationException;

import java.lang.reflect.ParameterizedType;

/**
 * Created by dmitry.rudenko on 9/25/2017.
 */
public abstract class Authorizer<Type, ID> {
  private Class<Type> instanceType;
  private Class<ID> idType;

  protected Authorizer() {
    instanceType = getClass(0);
    idType = getClass(1);
  }

  protected boolean checkId(ID id) {
    throw new AuthorizationException(this.getClass().getSimpleName() + ".checkId() is not implemented");
  }

  protected boolean checkInstance(Type instance) {
    throw new AuthorizationException(this.getClass().getSimpleName() + ".checkInstance() is not implemented");
  }

  protected ID stringToId(String id) {
    throw new AuthorizationException(this.getClass().getSimpleName() + ".stringToId() is not implemented");
  }

  @SuppressWarnings("unchecked")
  public boolean check(Object o) {
    if(o.getClass() == instanceType) {
      return checkInstance((Type) o);
    }
    if(o.getClass() == idType) {
      return checkId((ID) o);
    }
    if(o instanceof String) {
      return checkId(stringToId((String) o));
    }
    throw new AuthorizationException("Authorizer for type: " + o.getClass() + " is not implemented");
  }


  @SuppressWarnings("unchecked")
  private <T> Class<T> getClass(int index) {
    return ((Class<T>) ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[index]);
  }
}
