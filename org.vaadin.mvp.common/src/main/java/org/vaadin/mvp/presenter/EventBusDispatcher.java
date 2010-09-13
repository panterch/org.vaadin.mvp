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
    logger.info("DispatchEvent: {} ({})", name, event);
    Class<? extends EventBus> eventBusType = this.eventBus.getClass();
    // try exact method
    try {
      Method method = eventBusType.getMethod(name, new Class[] { event.getClass() });
      method.invoke(this.eventBus, event);
      return;
    } catch (Throwable e) {
      // ignore
    }
    // try zero args method
    try {
      Method method = eventBusType.getMethod(name);
      method.invoke(this.eventBus); // invoke without argument
      return;
    } catch (Throwable e) {
      // ignore
    }
    logger.error("Failed to dispatch event '{}' on {}, check that the event is " +
        "defined with appropriate signature", name, eventBus);
  }

  @Override
  public void dispatch(String name, Object... args) {
    logger.info("DispatchEvent: {} ({})", name, args);
  }

}
