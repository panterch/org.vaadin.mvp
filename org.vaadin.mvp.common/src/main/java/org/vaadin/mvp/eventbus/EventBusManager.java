package org.vaadin.mvp.eventbus;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Create new instances of a typed event bus.
 * 
 * FIXME: this needs cleanup.
 */
public class EventBusManager {

  // FIXME: check if needed, otherwise remove to not keep more references.
  // private Map<EventBus, EventBusHandler> busHandlers;

  private Map<Class<? extends EventBus>, EventBus> eventBusses;

  private EventReceiverRegistry handlerRegistry = new EventReceiverRegistry();

  public EventBusManager() {
    // busHandlers = new HashMap<EventBus, EventBusHandler>();
    eventBusses = new HashMap<Class<? extends EventBus>, EventBus>();
  }

  /**
   * Create an event bus of given <code>busType</code> and register the
   * <code>subscriber</code>.
   * 
   * @param <T>
   *          Event bus type (a Java interface type)
   * @param busType
   *          Event bus interface class
   * @param subscriber
   *          Subsriber to register
   * @return event bus instance
   */
  public <T extends EventBus> T register(Class<T> busType, Object subscriber) {
    if (!eventBusses.containsKey(busType)) {
      eventBusses.put(busType, create(busType));
    }
    this.handlerRegistry.addReceiver(subscriber);
    EventBus eventBus = eventBusses.get(busType);
    return (T) eventBus;
  }
  
  /**
   * Add a subscriber.
   * @param subscriber
   */
  public void addSubscriber(Object subscriber) {
    this.handlerRegistry.addReceiver(subscriber);
  }

  /**
   * 
   * @param <T>
   * @param busType
   * @return
   */
  public <T extends EventBus> T getEventBus(Class<? extends EventBus> busType) {
    assertEventBus(busType);
    return (T) eventBusses.get(busType);
  }

  /**
   * Assert an event bus of type <code>busType</code> exists. If no event bus
   * with required type is present, then an instance is created.
   * 
   * @param <T>
   *          Event bus type (a Java interface type)
   * @param busType
   *          Event bus interface class
   */
  private <T extends EventBus> void assertEventBus(Class<T> busType) {
    if (!eventBusses.containsKey(busType)) {
      eventBusses.put(busType, create(busType));
    }
  }

  /**
   * 
   * @param <T>
   * @param type
   * @return
   */
  protected <T extends EventBus> T create(Class<T> type) {
    EventBusHandler handler = new EventBusHandler(this, handlerRegistry, type.getName());
    T bus = (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, handler);
    // busHandlers.put(bus, handler);
    return bus;
  }

}
