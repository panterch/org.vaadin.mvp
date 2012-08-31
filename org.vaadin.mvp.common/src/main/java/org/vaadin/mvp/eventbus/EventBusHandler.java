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

    String eventName = method.getName();
    String eventHandlerName = "on" + eventName.substring(0, 1).toUpperCase() + eventName.substring(1);

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
        if (handlerMethod != null) {
          localHandlerMethodFound = true;
          handlerMethod.invoke(handler, args);
        }
      } catch (Throwable t) {
        logger.error("Failed to propagate event {} to handler {}", eventName, handlerType.getName());
        logger.error("When invocating the handler method an exception occurred", t);
        // CRAP!
        // MainEventBus mainBus = eventManager.getEventBus(MainEventBus.class);
        // mainBus.error(t);
      }
    }

    final boolean fallbackOnParent = parent != null;

    if (!localHandlerMethodFound && fallbackOnParent) {
      delegateToParent(method, args);
    }

    return null;
  }

  private Method lookupHandlerMethod(Method method, String eventName, String eventHandlerName, Object handler) {
    try {
      return handler.getClass().getMethod(eventHandlerName, method.getParameterTypes());
    } catch (Throwable t) {
      Object[] msgArgs = { handler.getClass().getName(), eventName, eventHandlerName };
      logger.warn("{} defined as a receiver for event {} but no method {}" +
          " could be found with matching arguments", msgArgs);
    }
    return null;
  }

  private void delegateToParent(Method method, Object[] args) {
    try {
      Method m = parent.getClass().getMethod(method.getName(), method.getParameterTypes());
      m.invoke(parent, args);
    } catch (Throwable t) {
      logger.error("Failed to invoke parent event bus", t);
    }
  }

}