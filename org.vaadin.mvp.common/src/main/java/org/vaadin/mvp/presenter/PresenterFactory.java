package org.vaadin.mvp.presenter;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.annotation.Presenter;
import org.vaadin.mvp.uibinder.UiBinder;
import org.vaadin.mvp.uibinder.UiBinderException;
import org.vaadin.mvp.uibinder.event.EventDispatcherBinder;

import com.vaadin.ui.Component;

/**
 * Simple PresenterFactory capable of creating presenters by class.
 * 
 * @author tam
 */
public class PresenterFactory extends AbstractPresenterFactory {

  /** Logger */
  static final Logger logger = LoggerFactory.getLogger(PresenterFactory.class);

  /** View factory */
  private IViewFactory viewFactory = new DefaultViewFactory();
  
  public PresenterFactory(EventBusManager ebm, Locale locale) {
    this.eventBusManager = ebm;
    this.locale = locale;
  }

  /**
   * Create an instance of a presenter and bind it's view.
   * 
   * @param arg
   * @return
   */
  protected IPresenter<?, ? extends EventBus> create(Object arg) {
    if (!(arg instanceof Class)) {
      throw new IllegalArgumentException("Object arg must be a class of type IPresenter.");
    }
    try {
      Class<IPresenter> presenterClass = (Class<IPresenter>) arg;
      IPresenter presenter = presenterClass.newInstance();
      presenter.setApplication(application);
      presenter.setMessageSource(messageSource);   
      Presenter def = presenterClass.getAnnotation(Presenter.class);

      if (def == null) {
        throw new IllegalArgumentException("Presenter class argument is missing annotation @Presenter");
      }

      EventBus bus = null;
      bus = createEventBus(presenterClass, presenter);
      presenter.setEventBus(bus);
      
      Object view = viewFactory.createView(eventBusManager, presenter, def.view(), locale);
      presenter.setView(view);
      
      presenter.bind();

      return presenter;
    } catch (Exception e) {
      logger.error("Failed to create presenter", e);
      if(e instanceof RuntimeException) {
        // re-throw
        throw (RuntimeException)e;
      }
    }
    return null;
  }

  @Override
  public IViewFactory getViewFactory() {
    return this.viewFactory;
  }

}
