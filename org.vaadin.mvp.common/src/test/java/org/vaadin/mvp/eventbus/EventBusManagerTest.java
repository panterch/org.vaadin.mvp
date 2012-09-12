package org.vaadin.mvp.eventbus;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * EventBusManager test class
 *
 * @author : apalumbo
 *
 */
public class EventBusManagerTest {

  private EventBusManager eventBusManager;

  @Before
  public void setUp() {
    eventBusManager = new EventBusManager();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetEventBus_privateEventBus_NotSupported() {
    eventBusManager.getEventBus(StubPrivateEventBus.class);
  }

  @Test
  public void testGetEventBus_NonPrivateEventBus_Supported() {
    eventBusManager.getEventBus(StubEventBus.class);
  }

  @Test
  public void testRegister_NonPrivateEventBus_mustReturnSameBus() {
    EventBus eventBus = eventBusManager.register(StubEventBus.class,new Object());
    EventBus eventBus2 = eventBusManager.register(StubEventBus.class,new Object());

    Assert.assertSame("Different event bus instances",eventBus,eventBus2);
  }

  @Test
  public void testRegister_NonPrivateEventBus_mustReturnDifferentBus() {
    EventBus eventBus = eventBusManager.register(StubPrivateEventBus.class,new Object());
    EventBus eventBus2 = eventBusManager.register(StubPrivateEventBus.class,new Object());

    Assert.assertNotSame("Same event bus instances",eventBus,eventBus2);
  }



}
