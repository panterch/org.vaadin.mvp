package org.vaadin.mvp.presenter;

import java.util.Locale;

import org.vaadin.mvp.eventbus.EventBusManager;


public interface IViewFactory {

  /**
   * Create a view instance.
   * @param <T>
   * @param ebm
   * @param presenter
   * @param viewType
   * @param locale
   * @return
   * @throws ViewFactoryException 
   */
  public abstract <T> T createView(EventBusManager ebm, IPresenter presenter,
      Class<T> viewType, Locale locale) throws ViewFactoryException;
  
  /**
   * 
   * @param viewType
   * @return
   */
  public abstract boolean canCreateView(Class<?> viewType);

}