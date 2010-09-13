package org.vaadin.mvp.presenter;

import java.util.Locale;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;


/**
 * Enhances the {@link BasePresenter} with capability to create other presenters
 * or just load other view parts to add on demand.
 * 
 * @author tam
 */
public abstract class FactoryPresenter<V, E extends EventBus> extends BasePresenter<V, E> implements IFactoryAwarePresenter {

  protected IPresenterFactory presenterFactory;

  /**
   * Create a view part instance using the presenter factorys underlying view
   * factory.
   * 
   * @param <T>
   *          View type
   * @param viewType
   *          View type class
   * @return Instance of the view
   * @throws ViewFactoryException
   */
  protected <T> T createView(Class<T> viewType) throws ViewFactoryException {
    boolean canCreateView = this.presenterFactory.getViewFactory().canCreateView(viewType);
    if (!canCreateView) {
      throw new ViewFactoryException("The underlying view factory is " +
          "not capable to create a view part of type '" + viewType.getName() + "'");
    }
    EventBusManager ebm = this.presenterFactory.getEventBusManager();
    Locale locale = this.presenterFactory.getLocale();
    T view = this.presenterFactory.getViewFactory().createView(ebm, this, viewType, locale);
    return view;
  }

  /**
   * Returns the locale associated with the presenter factory.
   * 
   * @return
   */
  protected Locale getLocale() {
    return this.presenterFactory.getLocale();
  }

  @Override
  public void setPresenterFactory(IPresenterFactory presenterFactory) {
    this.presenterFactory = presenterFactory;
  }
  
}
