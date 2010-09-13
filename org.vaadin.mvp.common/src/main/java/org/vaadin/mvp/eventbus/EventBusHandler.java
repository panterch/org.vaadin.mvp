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

  private EventBusManager eventManager;

  private EventBus parent;

  private EventReceiverRegistry handlerRegistry;

  private String busName;

  public EventBusHandler(EventBusManager em, EventReceiverRegistry hr, String name) {
    this.eventManager = em;
    this.handlerRegistry = hr;
    this.busName = name;
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
    Class<?>[] handlerTypes = eventDef.handlers();
    for (Class<?> handlerType : handlerTypes) {
      Object handler = handlerRegistry.lookupReceiver(handlerType);
      if (handler == null) {
        logger.info("Handler {} not registered", handlerType.getName());
        continue;
      }

      // invoke handler; the actual method is the event name prefixed with
      // "on..."
      String eventName = method.getName();
      Method handlerMethod = null;
      String eventHandlerName = null;
      try {
        eventHandlerName = "on" + eventName.substring(0, 1).toUpperCase() + eventName.substring(1);
        handlerMethod = handler.getClass().getMethod(eventHandlerName, method.getParameterTypes());
      } catch (Throwable t) {
        Object[] msgArgs = { handler.getClass().getName(), eventName, eventHandlerName };
        logger.warn("{} defined as a receiver for event {} but no method {}" +
            " could be found with matching arguments", msgArgs);
      }
      try {
        if (handlerMethod != null) {
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
    /* forwarding to parent disabled / there's no possibility to declare the parent */
    /*
    if (eventDef.forwardToParent() && parent != null) {
      try {
        Method m = parent.getClass().getMethod(method.getName(), method.getParameterTypes());
        m.invoke(parent, args);
      } catch (Throwable t) {
        logger.error("Failed to invoke parent event bus", t);
      }
    }
    */
    return null;
  }

}