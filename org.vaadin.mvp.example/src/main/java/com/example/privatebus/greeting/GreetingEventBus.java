package com.example.privatebus.greeting;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;
import org.vaadin.mvp.eventbus.annotation.PrivateEventBus;

/**
 * @author: apalumbo
 */
@PrivateEventBus
public interface GreetingEventBus extends EventBus{

  @Event(handlers = { GreetingPresenter.class })
  public void message(String messageText);
}
