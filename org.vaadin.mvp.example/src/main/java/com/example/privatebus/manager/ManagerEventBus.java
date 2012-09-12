package com.example.privatebus.manager;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

/**
 * @author: apalumbo
 */
public interface ManagerEventBus extends EventBus{

  @Event(handlers = ManagerPresenter.class)
  public void greetingFirst();

  @Event(handlers = ManagerPresenter.class)
  public void greetingSecond();


}
