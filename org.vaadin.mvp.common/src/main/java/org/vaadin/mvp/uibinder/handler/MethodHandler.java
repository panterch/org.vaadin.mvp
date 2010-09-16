package org.vaadin.mvp.uibinder.handler;

import java.lang.reflect.Method;

import com.vaadin.ui.Component;

/**
 * A handler to alternatively handle the adding of the child components with a
 * specific method name in the ComponentHandler. The method must be have
 * exactly one argument of a type which is assignable to com.vaadin.ui.Component
 * 
 * @author silvan
 * 
 */
public class MethodHandler implements TargetHandler {
  private ComponentHandler ch; // delegate

  private Method method;

  public MethodHandler(ComponentHandler ch) {
    this.ch = ch;
  }

  @Override
  public String getTargetNamespace() {
    return "urn:org.vaadin.mvp.uibinder.method";
  }

  @Override
  public void handleElementOpen(String uri, String name) {
    // find a appropriate method with a parameter of type Component.class
    method = findMethod(ch.getCurrent().getClass(), name);
    // no such method found
    if (method == null) {
      throw new IllegalArgumentException("The method " + name + " is missing in " + ch.getCurrent().getClass());
    }
    ch.setCurrentMethod(method);
  }

  @Override
  public void handleElementClose() {
    // not supported, event is just ignored
  }

  @Override
  public void handleAttribute(String name, Object value) {
    // maybe handle additional primitive type parameters
  }

  protected Method findMethod(Class<?> clazz, String name) {
    Class<?> searchType = clazz;
    while (searchType != null) {
      Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
      for (Method method : methods) {
        if (name.equals(method.getName()) && method.getParameterTypes().length == 1 && Component.class.isAssignableFrom(method.getParameterTypes()[0])) {
          return method;
        }
      }
      searchType = searchType.getSuperclass();
    }
    return null;
  }
}
