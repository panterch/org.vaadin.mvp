package org.vaadin.mvp.eventbus;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.mvp.eventbus.annotation.PrivateEventBus;

/**
 * Create new instances of a typed event bus.
 * 
 * FIXME: this needs cleanup.
 */
public class EventBusManager {

  // FIXME: check if needed, otherwise remove to not keep more references.
  // private Map<EventBus, EventBusHandler> busHandlers;

  private Map<Class<? extends EventBus>, EventBus> eventBusses;

  private IEventReceiverRegistry handlerRegistry = new EventReceiverRegistry();

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
    return this.register(busType,subscriber,null);
  }

  public <T extends EventBus> T register(Class<T> busType, Object subscriber,EventBus parentEventBus) {
    if (isPrivateEventBus(busType)) {
      return handlePrivateBus(busType,subscriber,parentEventBus);
    }

    return handleGlobalBus(busType, subscriber,parentEventBus);
  }

  private <T extends EventBus> boolean isPrivateEventBus(Class<T> busType) {
    return busType.getAnnotation(PrivateEventBus.class) != null;
  }

  private <T extends EventBus> T handleGlobalBus(Class<T> busType, Object subscriber,EventBus parentEventBus) {
    if (!eventBusses.containsKey(busType)) {
      eventBusses.put(busType, create(busType,parentEventBus));
    }
    this.handlerRegistry.addReceiver(subscriber);
    EventBus eventBus = eventBusses.get(busType);
    return busType.cast(eventBus);
  }

  protected <T extends EventBus> T handlePrivateBus(Class<T> type, Object subscriber,EventBus parentEventBus) {
    IEventReceiverRegistry privateHandlerRegistry = new EventReceiverRegistry();
    privateHandlerRegistry.addReceiver(subscriber);
    T bus = EventBusHandlerProxyFactory.createEventBusHandler(type, privateHandlerRegistry,parentEventBus);
    return bus;
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
  public <T extends EventBus> T getEventBus(Class<T> busType) {
    if (isPrivateEventBus(busType)) {
      throw new IllegalArgumentException("The bus " + busType + " is marked as private and it can be retrieved only from his presenter");
    }

    assertEventBus(busType);
    EventBus eventBus = eventBusses.get(busType);
    return busType.cast(eventBus);
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
      eventBusses.put(busType, create(busType,null));
    }
  }


  private <T extends EventBus> T create(Class<T> type, EventBus parentEventBus) {
    T bus = EventBusHandlerProxyFactory.createEventBusHandler(type, handlerRegistry,parentEventBus);
    // busHandlers.put(bus, handler);
    return bus;
  }



}
