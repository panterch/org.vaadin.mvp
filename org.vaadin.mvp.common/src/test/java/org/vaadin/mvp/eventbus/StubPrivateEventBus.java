package org.vaadin.mvp.eventbus;

import org.vaadin.mvp.eventbus.annotation.Event;
import org.vaadin.mvp.eventbus.annotation.PrivateEventBus;

/**
 * An event bus that has been marked private using the @PrivateEventBus annotation, is used
 * for test purposes
 *
 * @author: apalumbo
 *
 */
@PrivateEventBus
public interface StubPrivateEventBus extends EventBus {

  @Event
  public void selectMenuEntry(EventArgument dto);

  @Event
  public void niceEvent();

}
