package org.vaadin.mvp.eventbus;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;


public interface StubEventBus extends EventBus {

  @Event(handlers = { StubPresenter.class })
  public void selectMenuEntry(EventArgument dto);
  
  @Event(handlers = { StubPresenter.class, OtherPresenter.class })
  public void globalEvent(String event);

}
