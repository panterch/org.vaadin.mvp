package org.vaadin.mvp.uibinder.event;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reflection based event dispatcher dispatching all events to a single target.
 * 
 * @author tam
 */
public class ReflectiveEventDispatcher implements IEventDispatcher {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(ReflectiveEventDispatcher.class);

  private Object target;
  private String methodPrefix;

  /**
   * Constructor. Takes the event target.
   * 
   * @param target
   *          Object to dispatch events to (method calls)
   */
  public ReflectiveEventDispatcher(Object target) {
    this(target, null);
  }

  /**
   * Constructor. Takes the event target and additionaly a method prefix.
   * 
   * @param target
   *          Object to dispatch events to (method calls)
   * @param methodPrefix
   *          A method prefix, e.g. <code>on</code>.
   */
  public ReflectiveEventDispatcher(Object target, String methodPrefix) {
    if (target == null) {
      throw new NullPointerException("target must not be null"); // fail fast
    }
    this.target = target;
    this.methodPrefix = methodPrefix;
  }

  @Override
  public void dispatch(String name, Object event) {
    this.dispatch(name, event);
  }

  @Override
  public void dispatch(String name, Object... args) {
    Method method = findMethod(name, args);
    if(method != null) {
      try {
        method.invoke(method, args);
      } catch (Exception e) {
        logger.error("Failed to dispatch event, exception thrown", e);
      }
    }
  }

  /**
   * Find an appropriate method.
   * 
   * @param name
   *          Event name, i.e. the method name to call
   * @param args
   *          Arguments to the method
   * @return A method matching the event name and arguments or <code>null</code>
   *         if no appropriate method could be found
   */
  private Method findMethod(String name, Object... args) {
    Class<?>[] argTypes = createArgumentTypes(args);
    String methodName = this.methodPrefix == null ? name :
        this.methodPrefix + name.substring(0, 1).toUpperCase() + name.substring(1);
    try {
      Method method = target.getClass().getMethod(name, argTypes);
      return method;
    } catch (Exception e) {
      // ignore
    }
    // try zero argument method
    try {
      Method method = target.getClass().getMethod(methodName, new Class<?>[0]);
      return method;
    } catch (Exception e) {
      // no method found to dispatch the event, give up
    }
    logger.warn("Cant dispatch event: {} to target {}", name, target);
    return null;
  }

  /**
   * @param args
   * @return
   * @throws NullPointerException
   *           if any of the arguments is null
   */
  private Class<?>[] createArgumentTypes(Object[] args) throws NullPointerException {
    if (args == null) {
      return new Class<?>[0];
    }
    Class<?>[] types = new Class<?>[args.length];
    for (int i = 0; i < args.length; i++) {
      Object arg = args[i];
      // we can't handle null arguments
      types[i] = arg.getClass();
    }
    return types;
  }

}
