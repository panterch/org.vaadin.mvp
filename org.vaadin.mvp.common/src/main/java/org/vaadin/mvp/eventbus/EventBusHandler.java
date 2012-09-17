package org.vaadin.mvp.eventbus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.annotation.Event;


/**
 * EventBus invocation handler; delegates events to the actual listeners.
 * 
 * @author tam
 */
class EventBusHandler implements InvocationHandler {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(EventBusHandler.class);

  private EventBus parent;

  private IEventReceiverRegistry handlerRegistry;

  private String busName;

  public EventBusHandler(IEventReceiverRegistry hr, String name) {
    this(hr,name,null);
  }

  public EventBusHandler(IEventReceiverRegistry hr, String name, EventBus parent) {
    this.handlerRegistry = hr;
    this.busName = name;
    this.parent = parent;
  }

  /**
   * Propagate the event to all handlers.
   * 
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // handle toString()
    if ("toString".equals(method.getName())) {
      return "EventBus<" + busName + ">";
    }
    logger.info("Event received: {}", method.getName());
    Event eventDef = method.getAnnotation(Event.class);
    logger.info("Event annotation: {}", eventDef);

    boolean localHandlerMethodFound = false;
    final boolean fallbackOnParent = parent != null;

    String eventName = method.getName();
    String eventHandlerName = buildExpectedEventHandlerMethodName(eventName);

    Class<?>[] handlerTypes = eventDef.handlers();
    for (Class<?> handlerType : handlerTypes) {
      Object handler = handlerRegistry.lookupReceiver(handlerType);
      if (handler == null) {
        logger.info("Handler {} not registered", handlerType.getName());
        continue;
      }

      // invoke handler; the actual method is the event name prefixed with
      // "on..."

      Method handlerMethod = lookupHandlerMethod(method, eventName, eventHandlerName, handler);

      if (handlerMethod == null) {
        continue;
      }

      try {
        localHandlerMethodFound = true;
        handlerMethod.invoke(handler, args);
      } catch (Exception e) {
        logger.debug("Failed to propagate event {} to handler {}", eventName, handlerType.getName());
        throw new RuntimeException("During the invocations of the handler method an exception has occurred",e);
      }
    }

    if (!localHandlerMethodFound && fallbackOnParent) {
      delegateToParent(method.getName(),method.getParameterTypes(), args);
    }

    return null;
  }

  private String buildExpectedEventHandlerMethodName(String eventName) {
    return "on" + eventName.substring(0, 1).toUpperCase() + eventName.substring(1);
  }

  private Method lookupHandlerMethod(Method method, String eventName, String eventHandlerName, Object handler) {
    try {
      return handler.getClass().getMethod(eventHandlerName, method.getParameterTypes());
    } catch (Exception t) {
      Object[] msgArgs = { handler.getClass().getName(), eventName, eventHandlerName };
      logger.warn("{} defined as a receiver for event {} but no method {}" +
          " could be found with matching arguments", msgArgs);
    }
    return null;
  }

  private void delegateToParent(String methodName, Class<?>[] methodParameterTypes, Object[] args) {

    Method parentMethod = lookupMethodOnParent(methodName, methodParameterTypes);

    if (parentMethod == null) {
      return;
    }

    try {
      parentMethod.invoke(parent, args);
    } catch (Exception e) {
      throw new RuntimeException("Error occurred during method invocation",e);
    }
  }

  private Method lookupMethodOnParent(String methodName, Class<?>[] methodParameterTypes){
    try {
      return parent.getClass().getMethod(methodName, methodParameterTypes);
    } catch (NoSuchMethodException e) {
      // nothing to do
    } catch (SecurityException e) {
      // nothing to do
    }

    return null;
  }

}