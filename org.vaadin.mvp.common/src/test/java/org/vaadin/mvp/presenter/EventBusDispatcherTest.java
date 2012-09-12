package org.vaadin.mvp.presenter;

import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;

/**
 * @author: apalumbo
 */
public class EventBusDispatcherTest {

  private static final String THE_ARG = "theArg";
  private TestEventBus testEventBus;
  private EventBusDispatcher eventBusDispatcher;

  @Before
  public void setUp() throws Exception {
    testEventBus = createMock(TestEventBus.class);
    eventBusDispatcher = new EventBusDispatcher(null,testEventBus);
  }

  @Test
  public void testDispatch_withArgumentAndExistingMethod_willSuccess() {
    testEventBus.testEventWithArgument(THE_ARG);
    replay(testEventBus);
    eventBusDispatcher.dispatch("testEventWithArgument", THE_ARG);
    verify(testEventBus);
  }

  @Test
  public void testDispatch_withoutArgumentAndExistingMethod_willSuccess() {
    testEventBus.testEventWithoutArgument();
    replay(testEventBus);
    eventBusDispatcher.dispatch("testEventWithoutArgument");
    verify(testEventBus);
  }

  @Test(expected = Exception.class)
  public void testDispatch_withoutArgument_exceptionOccurred_willBeRaised() {
    testEventBus.testEventWithoutArgument();
    expectLastCall().andThrow(new RuntimeException("Faking an exception"));
    replay(testEventBus);
    eventBusDispatcher.dispatch("testEventWithoutArgument");
    verify(testEventBus);
  }

  @Test(expected = Exception.class)
  public void testDispatch_witArgument_exceptionOccurred_willBeRaised() {
    testEventBus.testEventWithArgument(THE_ARG);
    expectLastCall().andThrow(new RuntimeException("Faking an exception"));
    replay(testEventBus);
    eventBusDispatcher.dispatch("testEventWithArgument",THE_ARG);
    verify(testEventBus);
  }

  @Test
  public void testDispatch_notExistingMethod() {
    replay(testEventBus);
    eventBusDispatcher.dispatch("notExistingMethod",THE_ARG);
    verify(testEventBus);
  }



}
