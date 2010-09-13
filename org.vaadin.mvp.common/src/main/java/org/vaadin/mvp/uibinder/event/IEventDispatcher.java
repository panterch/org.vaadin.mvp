package org.vaadin.mvp.uibinder.event;

public interface IEventDispatcher {

  public void dispatch(String name, Object event);
  
  public void dispatch(String name, Object... args);
  
}
