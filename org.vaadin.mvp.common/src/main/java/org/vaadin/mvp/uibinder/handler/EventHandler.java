package org.vaadin.mvp.uibinder.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.uibinder.UiBinderException;
import org.vaadin.mvp.uibinder.UiConstraintException;
import org.vaadin.mvp.uibinder.event.EventBindingException;
import org.vaadin.mvp.uibinder.event.EventDispatcherBinder;
import org.vaadin.mvp.uibinder.event.IEventBinder;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * The {@link EventHandler} is responsible to bind listeners to components as
 * defined in the UI XML definition. The actual creation of a listener is
 * delegated to a handler factory (there is one handler factory per component
 * type (e.g. {@link Button}) that event binding is supported for.
 * 
 * @author tam
 */
public class EventHandler implements TargetHandler {

  /** Logger */
  static final Logger logger = LoggerFactory.getLogger(EventHandler.class);

  /** Component handler to access the component to bind listeners to */
  private ComponentHandler ch;

  /**
   * Event binder implementation.
   */
  private IEventBinder eventBinder = new EventDispatcherBinder();

  /**
   * Constructor.
   * 
   * @param ch
   *          Associated Component Handler.
   * @param eventBinder
   */
  public EventHandler(ComponentHandler ch, IEventBinder eventBinder) {
    this.ch = ch;
    if (eventBinder != null) {
      this.eventBinder = eventBinder;
    }
  }

  @Override
  public String getTargetNamespace() {
    return "urn:org.vaadin.mvp.uibinder.event";
  }

  @Override
  public void handleElementOpen(String uri, String name) throws UiBinderException {
    // ignored
  }

  @Override
  public void handleElementClose() {
    // ignored
  }

  /**
   * Bind an event listener to the current component.
   */
  @Override
  public void handleAttribute(String name, Object value) throws UiConstraintException {
    Component comp = ch.current;
    try {
      eventBinder.bindListener(comp, name, value);
    } catch (EventBindingException e) {
      throw new UiConstraintException("Failed to bind event " + name + " for component " + comp, e);
    }
  }

}
