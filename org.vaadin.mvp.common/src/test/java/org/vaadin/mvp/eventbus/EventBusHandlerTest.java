package org.vaadin.mvp.eventbus;

import junit.framework.Assert;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

import static org.easymock.EasyMock.*;

/**
 * Created by IntelliJ IDEA.
 * User: apalumbo
 * Date: 8/30/12
 * Time: 6:33 PM
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

    final BooleanHolder booleanHolder = new BooleanHolder();

    StubPrivateEventBus parentEventBus = createNiceMock(StubPrivateEventBus.class);
    parentEventBus.niceEvent();

    // this test has to be written in that way because no exception is forwarded by the EventBusHandler. This need to be
    // corrected. A strict mock will throw an exception in case of the event is not expected, the the handler will swallow this exception
    // so it is not possible to be sure if the method as been called or not.
    // When the swallow bug will be fixed it will be possible to use a strict mock for the test
    final IAnswer<Object> answer = createStubAnswer(booleanHolder);
    expectLastCall().andStubAnswer(answer);

    replay(mainEventReceiverRegistry, parentEventBus);

    StubEventBus eventBus = EventBusHandlerProxyFactory.createEventBusHandler(StubEventBus.class, mainEventReceiverRegistry,parentEventBus);

    Class<? extends EventBus> eventBusType = eventBus.getClass();
    Method method = eventBusType.getMethod("niceEvent");
    method.invoke(eventBus); // invoke without argument

    verify(mainEventReceiverRegistry,parentEventBus);

    Assert.assertTrue("Parent bus event not called",booleanHolder.methodCalled);

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

  private IAnswer<Object> createStubAnswer(final BooleanHolder booleanHolder) {
    return new IAnswer<Object>() {
        @Override
        public Object answer() throws Throwable {
          booleanHolder.methodCalled = true;
          return null;
        }
      };
  }

  private static class BooleanHolder {

    public boolean methodCalled = false;

  }




}
