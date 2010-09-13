package org.vaadin.mvp.presenter.spring;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.AbstractPresenterFactory;
import org.vaadin.mvp.presenter.IPresenterFactory;

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

  private EventBusManager eventBusManager = new EventBusManager();

  @Override
  public final void init() {
    preInit();
    presenterFactory.setEventManager(eventBusManager);
    //    Locale locale = getLocale();
    //    presenterFactory.setLocale(locale);
    postInit();
  }

  public abstract void preInit();
  
  public abstract void postInit();

  public IPresenterFactory getPresenterFactory() {
    return presenterFactory;
  }

}
