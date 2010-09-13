package org.vaadin.mvp.presenter;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBusManager;


/**
 * Abstract view factory. In contrast to the presenter factory the view factory
 * is stateless.
 * 
 * @author tam
 * 
 */
public abstract class AbstractViewFactory implements IViewFactory {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(AbstractViewFactory.class);

  /* (non-Javadoc)
   * @see org.vaadin.mvp.presenter.IViewFactory#createView(org.vaadin.mvp.eventbus.EventBusManager, org.vaadin.mvp.presenter.IPresenter, java.lang.Class, java.util.Locale)
   */
  @Override
  public abstract <T> T createView(EventBusManager ebm, IPresenter presenter, 
      Class<T> viewType, Locale locale) throws ViewFactoryException;

  /*
   * (non-Javadoc)
   * @see org.vaadin.mvp.presenter.IViewFactory#canCreateView(java.lang.Class)
   */
  @Override
  public boolean canCreateView(Class<?> viewType) {
    return true;
  }
  
}
