package org.vaadin.mvp.eventbus;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link EventBusHandler}.
 * 
 * @author apalumbo
 */
public class EventBusHandlerTest {

  private static final String SAMPLE_EVENT = "sampleEvent";
  private EventBusHandler eventBusHandler;

  private IEventReceiverRegistry mainEventReceiverRegistry;

  @Before
  public void setUp() {
    mainEventReceiverRegistry = createMock(IEventReceiverRegistry.class);
  }


  @Test
  public void testGlobalEventsDelivery() {

    StubPresenter stubPresenter = createMock(StubPresenter.class);
    expect(mainEventReceiverRegistry.lookupReceiver(StubPresenter.class)).andReturn(stubPresenter);
    stubPresenter.onGlobalEvent(SAMPLE_EVENT);

    OtherPresenter otherPresenter = createMock(OtherPresenter.class);
    expect(mainEventReceiverRegistry.lookupReceiver(OtherPresenter.class)).andReturn(otherPresenter);
    otherPresenter.onGlobalEvent(SAMPLE_EVENT);

    replay(mainEventReceiverRegistry,stubPresenter,otherPresenter);

    StubEventBus eventBus = EventBusHandlerProxyFactory.createEventBusHandler(StubEventBus.class, mainEventReceiverRegistry, null);
    eventBus.globalEvent(SAMPLE_EVENT);

    verify(mainEventReceiverRegistry,stubPresenter,otherPresenter);

  }

  @Test
  public void testEventsDelivery() {

    final EventArgument eventArgument = new EventArgument(1l,"test");

    StubPresenter stubPresenter = createMock(StubPresenter.class);
    expect(mainEventReceiverRegistry.lookupReceiver(StubPresenter.class)).andReturn(stubPresenter);
    stubPresenter.onSelectMenuEntry(eventArgument);

    replay(mainEventReceiverRegistry,stubPresenter);

    StubEventBus eventBus = EventBusHandlerProxyFactory.createEventBusHandler(StubEventBus.class, mainEventReceiverRegistry, null);

    eventBus.selectMenuEntry(eventArgument);

    verify(mainEventReceiverRegistry,stubPresenter);

  }

  @Test
  public void testParentFallbackEventsDelivery_notExistingEventOnChildWillBeForwarded() throws Exception{

    StubPrivateEventBus parentEventBus = createNiceMock(StubPrivateEventBus.class);
    parentEventBus.niceEvent();

    replay(mainEventReceiverRegistry, parentEventBus);

    StubEventBus eventBus = EventBusHandlerProxyFactory.createEventBusHandler(StubEventBus.class, mainEventReceiverRegistry,parentEventBus);

    Class<? extends EventBus> eventBusType = eventBus.getClass();
    Method method = eventBusType.getMethod("niceEvent");
    method.invoke(eventBus); // invoke without argument

    verify(mainEventReceiverRegistry,parentEventBus);

  }

  @Test
  public void testParentFallbackEventsDelivery_existingEventOnChildWillNotBeForwarded() throws Exception{

    final EventArgument eventArgument = new EventArgument(1l,"test");

    StubPresenter stubPresenter = createMock(StubPresenter.class);
    expect(mainEventReceiverRegistry.lookupReceiver(StubPresenter.class)).andReturn(stubPresenter);
    stubPresenter.onSelectMenuEntry(eventArgument);

    StubPrivateEventBus parentEventBus = createNiceMock(StubPrivateEventBus.class);

    replay(mainEventReceiverRegistry, parentEventBus,stubPresenter);

    StubEventBus eventBus = EventBusHandlerProxyFactory.createEventBusHandler(StubEventBus.class, mainEventReceiverRegistry,parentEventBus);

    eventBus.selectMenuEntry(eventArgument);

    verify(mainEventReceiverRegistry,parentEventBus,stubPresenter);

  }

  @Test(expected = Exception.class)
  public void testParentFallbackEventsDelivery_existingEventOnChild_exceptionThrown() throws Exception{

    final EventArgument eventArgument = new EventArgument(1l,"test");

    StubPresenter stubPresenter = createMock(StubPresenter.class);
    expect(mainEventReceiverRegistry.lookupReceiver(StubPresenter.class)).andReturn(stubPresenter);
    stubPresenter.onSelectMenuEntry(eventArgument);
    expectLastCall().andThrow(new RuntimeException());

    StubPrivateEventBus parentEventBus = createNiceMock(StubPrivateEventBus.class);

    replay(mainEventReceiverRegistry, parentEventBus,stubPresenter);

    StubEventBus eventBus = EventBusHandlerProxyFactory.createEventBusHandler(StubEventBus.class, mainEventReceiverRegistry,parentEventBus);

    eventBus.selectMenuEntry(eventArgument);

    verify(mainEventReceiverRegistry,parentEventBus,stubPresenter);

  }

  @Test(expected = Exception.class)
  public void testParentFallbackEventsDelivery_notExistingEventOnChildWillBeForwarded_exceptionIsThrown() throws Exception{

    StubPrivateEventBus parentEventBus = createNiceMock(StubPrivateEventBus.class);
    parentEventBus.niceEvent();
    expectLastCall().andThrow(new RuntimeException());

    replay(mainEventReceiverRegistry, parentEventBus);

    StubEventBus eventBus = EventBusHandlerProxyFactory.createEventBusHandler(StubEventBus.class, mainEventReceiverRegistry,parentEventBus);

    Class<? extends EventBus> eventBusType = eventBus.getClass();
    Method method = eventBusType.getMethod("niceEvent");
    method.invoke(eventBus); // invoke without argument

    verify(mainEventReceiverRegistry,parentEventBus);

  }

}
