package com.example.spring.main;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

import com.example.mvp.ExampleSpringApp;

public interface SpringMainEventBus extends EventBus {

  @Event(handlers = { SpringMainPresenter.class })
  public void start(ExampleSpringApp app);
  
}
