package org.vaadin.mvp.presenter;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.uibinder.UiBinder;
import org.vaadin.mvp.uibinder.UiBinderException;
import org.vaadin.mvp.uibinder.event.EventDispatcherBinder;

import com.vaadin.ui.Component;

/**
 * Simple default implementation of a view factory.
 * 
 * @author tam
 */
public class DefaultViewFactory extends AbstractViewFactory {

  private UiBinderViewFactory binderFactory = new UiBinderViewFactory();
  
  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(DefaultViewFactory.class);
  
  @Override
  public <T> T createView(EventBusManager ebm, IPresenter presenter, Class<T> viewType, Locale locale) throws ViewFactoryException {
    logger.debug("View class: {}", viewType.getName());
    if (binderFactory.canCreateView(viewType)) {
      T view = binderFactory.createView(ebm, presenter, viewType, locale);
      return view;
    } else {
      // create the view
      T view;
      try {
        view = viewType.newInstance();
      } catch (Exception e) {
        throw new ViewFactoryException("Failed to create view", e);
      }
      return view;
    }
  }

  public UiBinderViewFactory getBinderFactory() {
    return binderFactory;
  }

  public void setBinderFactory(UiBinderViewFactory binderFactory) {
    this.binderFactory = binderFactory;
  }
  
  
  
}
