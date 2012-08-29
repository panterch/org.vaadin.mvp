package org.vaadin.mvp.uibinder.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.uibinder.UiBinder;
import org.vaadin.mvp.uibinder.UiBinderException;
import org.vaadin.mvp.uibinder.UiConstraintException;
import org.vaadin.mvp.uibinder.event.IEventBinder;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;

/**
 * Component Handler is main handler for UiBinder parse events. All elements
 * that do not belong to a defined namespace, are handled by the component
 * handler.
 * 
 * <p>
 * The component handler then uses the actual namespace in the form
 * <code>urn:import:package-name</code> (e.g.
 * <code>urn:import:org.vaadin.mvp.component</code>) as the package information,
 * the elements local name is expected to be a class in the package. On start of
 * an element, the component handler tries to create an instance of the class'
 * fully qualified name (package + name).
 * </p>
 * 
 * @author tam
 * 
 */
public class ComponentHandler implements TargetHandler {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(ComponentHandler.class);

  private static int instanceCount = 0;

  private int instance = instanceCount++;

  /**
   * The UiBinder instance that triggered the bind operation (required to bind
   * nested components)
   */
  private UiBinder uiBinder;

  /**
   * The associated event binder (required to bind sub views).
   */
  private IEventBinder eventBinder;

  private Locale locale;

  /** Component stack */
  private Stack<Component> components = new Stack<Component>();

  /** Map of namespaci uri to package */
  private Map<String, String> packages = new HashMap<String, String>();

  /** The current component being built. */
  private Component current = null;

  /** The method to add the current component to the parent. Can be null. */
  private Method currentMethod;

  /**
   * Constructor.
   * 
   * @param uiBinder
   * @param view
   * @param locale
   */
  public ComponentHandler(UiBinder uiBinder, Component view, Locale locale) {
    this.uiBinder = uiBinder;
    this.locale = locale;
    current = view;
  }

  public IEventBinder getEventBinder() {
    return eventBinder;
  }

  public void setEventBinder(IEventBinder eventBinder) {
    this.eventBinder = eventBinder;
  }

  @Override
  public String getTargetNamespace() {
    return null;
  }

  public Component getRootComponent() {
    return components.get(0);
  }

  @Override
  public void handleElementOpen(String uri, String name) throws UiBinderException {
    components.push(current);
    logger.debug("handleElement: {} {}", uri, name);
    String viewName = null;
    try {
      String packageName = namespaceUriToPackageName(uri);
      if (Character.isUpperCase(name.charAt(0))) {
        try {
          Component comp = null;
          viewName = packageName + "." + name;
          if (uiBinder.isBindable(viewName)) {
            logger.debug("[{}] Creating component with UiBinder", instance);
            comp = uiBinder.bind(viewName, locale, this.eventBinder);
          } else {
            logger.debug("[{}] Creating component with class: {}", instance, viewName);
            Class<?> uiClass = Class.forName(viewName);
            comp = (Component) uiClass.newInstance();
          }
          current = comp;
        } catch (ClassNotFoundException e) {
          throw new UiConstraintException("No component could be created with name: " + viewName
              + " - the component is neither instantiable nor bindable from XML definition");
        }
      } else {
        // FIXME: what to do here?
      }
    } catch (InstantiationException e) {
      throw new UiConstraintException("Cannot instantiate component type: " + viewName, e);
    } catch (IllegalAccessException e) {
      throw new UiConstraintException("Cannot instantiate component type: " + viewName, e);
    }
  }

  /**
   * Convert a namespace URI to package name; supported schemes are
   * <code>urn</code> and <code>http</code>.
   * 
   * @param uri
   * @return
   * @throws UiConstraintException
   */
  protected String namespaceUriToPackageName(String uri) throws UiConstraintException {
    if (packages.containsKey(uri)) {
      return packages.get(uri);
    }
    try {
      URI nsUri = new URI(uri);
      String pkg = null;
      String scheme = nsUri.getScheme();
      if ("urn".equals(scheme)) {
        pkg = nsUri.getSchemeSpecificPart();
      }
      /* maybe disable support for http scheme? */
      else if ("http".equals(scheme)) {
        String[] hostParts = nsUri.getHost().split("\\.");
        String[] pathParts = nsUri.getPath().substring(1).split("/");
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<String>(Arrays.asList(hostParts));
        Collections.reverse(list);
        list.addAll(Arrays.asList(pathParts));
        if (list.size() > 0) {
          sb.append(list.get(0));
          for (int i = 1; i < list.size(); i++) {
            sb.append(".").append(list.get(i));
          }
        }
        pkg = sb.toString();
      } else {
        throw new UiConstraintException("Unsupported namespace URI scheme: " + scheme + " (URI = " + uri + ")");
      }
      if (pkg.startsWith("import:")) {
        pkg = pkg.substring("import:".length());
      }
      packages.put(uri, pkg);
      return pkg;
    } catch (URISyntaxException e) {
      throw new UiConstraintException("Invalid namespace URI", e);
    }
  }

  @Override
  public void handleElementClose() throws UiBinderException {
    Component inner = current;
    current = components.pop(); // should always be one on the stack

    logger.debug("Adding element {} to {}", inner.getClass().getName(), current.getClass().getName());

    // add the inner component to the current
    if (currentMethod != null) {
      try {
        if (inner instanceof Component) {
          currentMethod.invoke(current, inner);
        }
      } catch (IllegalArgumentException e) {
        throw new UiBinderException("IllegalArgumentException: Cannot add component " + inner.getClass().getName() + " with method "
            + this.currentMethod.getName() + "(" + currentMethod.getParameterTypes()[0].getName() + ")", e);
      } catch (IllegalAccessException e) {
        throw new UiBinderException("IllegalAccessException: Cannot add component " + inner.getClass().getName() + " with method "
            + this.currentMethod.getName() + "(" + currentMethod.getParameterTypes()[0].getName() + ")", e);
      } catch (InvocationTargetException e) {
        throw new UiBinderException("InvocationTargetException: Cannot ad component " + inner.getClass().getName() + " with method "
            + this.currentMethod.getName() + "(" + currentMethod.getParameterTypes()[0].getName() + ")", e);
      }
      logger.debug("Add current {} with Method {}", current.getClass().getName(), this.currentMethod.getName());
      currentMethod = null;
    } else {
      if (current instanceof Panel && inner instanceof ComponentContainer) {
        // set content
        ComponentContainer panelContent = ((Panel) current).getContent();
        if (panelContent != null) {
          logger.warn("Panel content is already set, content is overwritten with new component");
        }
        logger.debug("Current {} is a PANEL, setting content: {}", current.getClass().getName(), inner.getClass().getName());
        ((Panel) current).setContent((ComponentContainer) inner);
      } else if (current instanceof ComponentContainer) {
        // add the component
        logger.debug("Current {} is a container, adding new comp: {}", current.getClass().getName(), inner.getClass().getName());
        ((ComponentContainer) current).addComponent(inner);
      }
    }
    logger.debug("Element closed, current element now is: {}", current.getClass().getName());
  }

  @Override
  public void handleAttribute(String name, Object value) {
    if (current == null) {
      return;
    }
    logger.debug("Trying to set attribute {} with value {} on current", name, value);
    Method setter = findApplicableSetterMethod(current, name, value);
    if (setter == null) {
      logger.warn("No method found to set property: {} of type {}", name, value.getClass().getName());
      return;
    }
    try {
      if (setter.getParameterTypes().length == 0) {
        setter.invoke(current);
        return;
      }
      Class<?> argType = setter.getParameterTypes()[0];
      setter.invoke(current, ConvertUtils.convert(value, argType));
    } catch (Exception e) {
      logger.error("Failed to invoke setter method: " + setter.getName(), e);
    }
  }

  public Component getCurrent() {
    return current;
  }

  /**
   * Find the best matching method to set values on the target.
   * 
   * @param target
   * @param propertyName
   * @param value
   * @return
   */
  protected Method findApplicableSetterMethod(Object target, String propertyName, Object value) {
    // check if muptiple args
    String methodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    Method[] methods = target.getClass().getMethods();
    if (value.getClass().isArray()) {
      // handle multiple args

      // a) is there a method with single argument type Array and matching
      // component type?

      // b) is these a method taking as many args as elements in the array?
    } else {
      Method method = MethodUtils.getAccessibleMethod(target.getClass(), methodName, value.getClass());
      if (method != null) {
        return method;
      }
      // find another method
      for (Method m : methods) {
        if (!m.getName().equals(methodName)) {
          continue;
        }
        Class<?>[] parameterTypes = m.getParameterTypes();
        if (parameterTypes.length != 1) {
          continue;
        }
        Class<?> mpt = parameterTypes[0];
        if (mpt.isAssignableFrom(value.getClass())) {
          return m;
        }

        Converter converter = ConvertUtils.lookup(mpt);
        if (converter != null) {
          return m;
        }
      }
    }
    // still no method; try zero arg method if value is empty
    if (value == null || value.toString().length() == 0) {
      for (Method m : methods) {
        if (m.getName().equals(methodName) && m.getParameterTypes().length == 0) {
          return m;
        }
      }
    }
    return null;
  }

  /**
   * Set the method to add the current component to the parent. The standard
   * will be used when not set and the method will be cleared after
   * {@link #handleElementClose()}.
   * 
   * @param currentMethod
   */
  public void setCurrentMethod(Method currentMethod) {
    this.currentMethod = currentMethod;
  }

  protected Method getCurrentMethod() {
    return currentMethod;
  }
}
