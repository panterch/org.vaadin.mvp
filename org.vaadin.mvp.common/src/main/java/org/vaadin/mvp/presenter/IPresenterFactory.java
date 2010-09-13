package org.vaadin.mvp.presenter;

import java.util.Locale;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;


/**
 * Contract for presenter factories.
 * 
 * @author tam
 *
 */
public interface IPresenterFactory {

  /**
   * Create a new instance of a presenter with it's view and event bus setup.
   * 
   * @param arg
   *          identifier of the presenter to create (e.g. a name, class, etc.)
   * @return new instance of the presenter
   */
  public abstract IPresenter<?, ? extends EventBus> createPresenter(Object arg);
  
  /**
   * Returns the view factory.
   * 
   * @return
   */
  public abstract IViewFactory getViewFactory();

  /**
   * Returns the event bus manager.
   * @return
   */
  public abstract EventBusManager getEventBusManager();
  
  /**
   * Returns the locale.
   * 
   * @return
   */
  public abstract Locale getLocale();

}