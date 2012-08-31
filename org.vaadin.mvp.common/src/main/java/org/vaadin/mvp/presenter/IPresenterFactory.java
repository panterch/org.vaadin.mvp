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
   * Create a new instance of a presenter with it's view and event bus setup, if a parentEventBus is provided it will
   * be used as parent bus this will enable a fallback of the event not handled directly from the newly created event bus
   *
   * @param arg
   *          identifier of the presenter to create (e.g. a name, class, etc.)
   * @param parentEventBus
   *          the bus that will be used as parent
   * @return new instance of the presenter
   */
  public abstract IPresenter<?, ? extends EventBus> createPresenter(Object arg, EventBus parentEventBus);

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