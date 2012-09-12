package org.vaadin.mvp.eventbus;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.annotation.Event;


public class EventBusHandlerIntegrationTest {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(EventBusHandlerIntegrationTest.class);
  private StubEventBus instance;
  private StubPresenter presenter;
  private OtherPresenter otherPresenter;

  @Before
  public void setUp() {
    EventBusManager eventBusManager = new EventBusManager();
    presenter = new StubPresenter();
    otherPresenter = new OtherPresenter();
    eventBusManager.addSubscriber(presenter);
    eventBusManager.addSubscriber(otherPresenter);
    instance = eventBusManager.getEventBus(StubEventBus.class);
  }

  @Test
  public void testAnnotations() {
    Method[] events = StubEventBus.class.getMethods();
    for (Method event : events) {
      logger.info("Event method: {} - handlers: ", event.getName());
      Event ea = event.getAnnotation(Event.class);
      for (Class<?> handler : ea.handlers()) {
        logger.info("- {}", handler.getName());
      }
    }
  }

  @Test
  public void testFireEvent() {
    instance.selectMenuEntry(new EventArgument(1l, "TestMenu"));
    assertTrue("event has not been propagated to presenter", presenter.eventReceived);
    assertFalse("event should not have gone to the other presenter", otherPresenter.received);
  }
  
  @Test
  public void testFireGlobalEvent() {
    instance.globalEvent("Message to every body!");
    assertTrue("event has not been propagated to presenter", presenter.eventReceived);
    assertTrue("event has not been propagated to other presenter", otherPresenter.received);
  }

}
