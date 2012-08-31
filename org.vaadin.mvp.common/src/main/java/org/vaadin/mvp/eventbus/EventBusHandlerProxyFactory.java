package org.vaadin.mvp.eventbus;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: apalumbo
 * Date: 8/30/12
 * Time: 6:55 PM
 */
public class EventBusHandlerProxyFactory {

  public static <T extends EventBus> T createEventBusHandler(Class<T> type, IEventReceiverRegistry eventReceiverRegistry, EventBus parentEventBus) {

    final Class[] classes = createInterfacesArray(type, parentEventBus);
    EventBusHandler handler = new EventBusHandler(eventReceiverRegistry, type.getName(),parentEventBus);
    return (T) Proxy.newProxyInstance(type.getClassLoader(), classes, handler);
  }

  private static <T extends EventBus> Class[] createInterfacesArray(Class<T> type, EventBus parentEventBus) {
    List<Class> eventBusClasses = new ArrayList<Class>();
    eventBusClasses.add(type);
    if (parentEventBus != null) {
      final Class<?>[] interfaces = parentEventBus.getClass().getInterfaces();
      for (Class clazz : interfaces) {
        if (EventBus.class.isAssignableFrom(clazz)) {
          eventBusClasses.add(clazz);
        }
      }
    }

    return eventBusClasses.toArray(new Class[]{});
  }
}
