package org.vaadin.mvp.eventbus;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for the creation of the handler of the event bus. Java dynamic proxies will be used
 * delegating the execution to a EventBusHandler. The dynamic proxy created and returned will
 * implement the main event bus interface and, if provided, also the parent event bus interface. This is useful for the
 * fallback dispatching (forward to parent).
 *
 * @author: apalumbo
 */
public class EventBusHandlerProxyFactory {

  public static <T extends EventBus> T createEventBusHandler(Class<T> type, IEventReceiverRegistry eventReceiverRegistry, EventBus parentEventBus) {

    final Class<?>[] classes = createInterfacesArray(type, parentEventBus);
    EventBusHandler handler = new EventBusHandler(eventReceiverRegistry, type.getName(),parentEventBus);
    Object handlerProxy = Proxy.newProxyInstance(type.getClassLoader(), classes, handler);
    return type.cast(handlerProxy);
  }

  private static <T extends EventBus> Class<?>[] createInterfacesArray(Class<T> type, EventBus parentEventBus) {
    List<Class<?>> eventBusClasses = new ArrayList<Class<?>>();
    eventBusClasses.add(type);
    if (parentEventBus != null) {
      final Class<?>[] interfaces = parentEventBus.getClass().getInterfaces();
      for (Class<?> clazz : interfaces) {
        if (EventBus.class.isAssignableFrom(clazz)) {
          eventBusClasses.add(clazz);
        }
      }
    }

    return eventBusClasses.toArray(new Class[]{});
  }
}
