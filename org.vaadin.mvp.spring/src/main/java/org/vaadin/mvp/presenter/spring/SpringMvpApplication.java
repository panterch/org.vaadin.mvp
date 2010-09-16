package org.vaadin.mvp.presenter.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.AbstractPresenterFactory;
import org.vaadin.mvp.presenter.IPresenterFactory;
import org.vaadin.mvp.uibinder.IUiMessageSource;

import com.vaadin.Application;

/**
 * Vaadin base application class for spring/mvp based applications.
 * 
 * @author tam
 * 
 */
public abstract class SpringMvpApplication extends Application {

  @Autowired(required = true)
  protected AbstractPresenterFactory presenterFactory;

  protected IUiMessageSource messageSource; 
  
  private EventBusManager eventBusManager = new EventBusManager();

  @Override
  public final void init() {
    preInit();
    presenterFactory.setEventManager(eventBusManager);
    
    presenterFactory.setApplication(this);
    presenterFactory.setMessageSource(messageSource);    
    
    //    Locale locale = getLocale();
    //    presenterFactory.setLocale(locale);
    postInit();
  }

  public abstract void preInit();
  
  public abstract void postInit();

  public IPresenterFactory getPresenterFactory() {
    return presenterFactory;
  }

  protected IUiMessageSource getMessageSource() {
    return messageSource;
  }
  protected void setMessageSource(IUiMessageSource messageSource) {
    this.messageSource = messageSource;
  }

}
