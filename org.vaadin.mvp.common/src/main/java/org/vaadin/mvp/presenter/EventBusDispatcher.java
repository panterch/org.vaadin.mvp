package org.vaadin.mvp.presenter;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.uibinder.event.IEventDispatcher;


/**
 * A dispatcher for IEventBinder used by UiBinder that binds events declared in
 * UI XML to the event bus instead of directly binding them to some
 * implementation class.
 * 
 * @author tam
 */
public class EventBusDispatcher implements IEventDispatcher {

  private static class ResolvedMethodDetails {

    private Method resolvedMethod;

    private boolean methodHasArguments;

    private ResolvedMethodDetails(Method resolvedMethod, boolean methodHasArguments) {
      this.resolvedMethod = resolvedMethod;
      this.methodHasArguments = methodHasArguments;
    }

    public Method getResolvedMethod() {
      return resolvedMethod;
    }

    public void setResolvedMethod(Method resolvedMethod) {
      this.resolvedMethod = resolvedMethod;
    }

    public boolean getMethodHasArguments() {
      return methodHasArguments;
    }

    public void setMethodHasArguments(boolean methodHasArguments) {
      this.methodHasArguments = methodHasArguments;
    }
  }

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(EventBusDispatcher.class);

  private EventBusManager eventBusManager;
  private EventBus eventBus;

  public EventBusDispatcher(EventBusManager ebManager, EventBus eb) {
    this.eventBusManager = ebManager;
    this.eventBus = eb;
  }

  @Override
  public void dispatch(String name, Object event) {
    logger.debug("DispatchEvent: {} ({})", name, event);

    final Object[] args = event!=null?new Object[]{event}:new Object[]{};
    dispatch(name, args);
  }

  @Override
  public void dispatch(String name, Object... args) {
    Class[] argumentsClasses = extractArgumentClasses(args);

    Class<? extends EventBus> eventBusType = this.eventBus.getClass();

    ResolvedMethodDetails resolvedMethodDetails = findTargetMethod(name,eventBusType,argumentsClasses);

    if (resolvedMethodDetails == null) {
      logger.error("Failed to dispatch event '{}' on {}, check that the event is " +
          "defined with appropriate signature", name, eventBus);
      return;
    }

    invokeMethod(args, resolvedMethodDetails);

  }

  private Class[] extractArgumentClasses(Object[] args) {
    Class[] argumentClasses = new Class[args.length];

    for (int i = 0; i < args.length; i++) {
      argumentClasses[i] = args[i].getClass();
    }

    return argumentClasses;
  }

  private void invokeMethod(Object[] args, ResolvedMethodDetails resolvedMethodDetails) {
    Object[] invocationArguments = resolvedMethodDetails.getMethodHasArguments()?args:new Object[]{};

    Method method = resolvedMethodDetails.getResolvedMethod();
    try {
      method.invoke(this.eventBus, invocationArguments);
    } catch (Exception e) {
      throw new RuntimeException("An error occurred during the method execution",e);
    }
  }

  private ResolvedMethodDetails findTargetMethod(String name,Class<? extends EventBus> eventBusType, Class[] argumentsClasses) {
    Method foundMethod = tryMethodLookup(name,eventBusType, argumentsClasses);
    if (foundMethod != null) {
      return new ResolvedMethodDetails(foundMethod,true);
    }

    foundMethod = tryMethodLookup(name,eventBusType, new Class[]{});
    if (foundMethod != null) {
      return new ResolvedMethodDetails(foundMethod,false);
    }

    return null;

  }

  private Method tryMethodLookup(String name,Class<? extends EventBus> eventBusType,Class[] argumentsClasses) {
    try {
      return eventBusType.getMethod(name, argumentsClasses);
    } catch (SecurityException e) {
      logger.warn("The method " + name + " and arguments has been found on " + eventBusType + " but is not accessible");
    } catch (NoSuchMethodException e) {
      // nothing to do
    }

    return null;
  }

}
