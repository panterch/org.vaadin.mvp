package org.vaadin.mvp.uibinder.event;

import com.vaadin.ui.Component;

/**
 * Contract for <code>EventBinder</code>s. An EventBinder is used to bind event
 * listeners to components bound with UiBinder.
 * 
 * @author tam
 */
public interface IEventBinder {

  /**
   * Bind an event (<code>name</code>) to the component <code>comp</code>.
   * 
   * @param comp
   * @param eventName
   * @param targetEvent
   * 
   * @throws EventBindingException
   */
  public abstract void bindListener(Component comp, String eventName, Object targetEvent) throws EventBindingException;

}