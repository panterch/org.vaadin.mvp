package org.vaadin.mvp.eventbus;

import org.vaadin.mvp.eventbus.annotation.Event;
import org.vaadin.mvp.eventbus.annotation.PrivateEventBus;

/**
 * Created by IntelliJ IDEA.
 * User: apalumbo
 * Date: 8/30/12
 * Time: 5:18 PM
 */
@PrivateEventBus
public interface StubPrivateEventBus extends EventBus {

  @Event
  public void selectMenuEntry(EventArgument dto);

  @Event
  public void niceEvent();

}
