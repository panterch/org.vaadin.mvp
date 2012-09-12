package com.example.mvp;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.IPresenterFactory;
import org.vaadin.mvp.presenter.PresenterFactory;

import com.example.main.MainEventBus;
import com.example.main.MainPresenter;
import com.vaadin.Application;

public class ExampleApp extends Application {

  /** Per application (session) event bus manager */
  private EventBusManager ebm = new EventBusManager();
  /** Per application presenter factory */
  private PresenterFactory presenterFactory;
  
  /** Main presenter */
  private IPresenter<?, ? extends EventBus> mainPresenter;
  
  @Override
  public void init() {
    // create an instance of a default presenter factory
    this.presenterFactory = new PresenterFactory(ebm, getLocale());
    this.presenterFactory.setApplication(this);
    
    // request an instance of MainPresenter
    mainPresenter = this.presenterFactory.createPresenter(MainPresenter.class);
    MainEventBus eventBus = (MainEventBus) mainPresenter.getEventBus();
    eventBus.start(this);
    
  }
  
  public IPresenterFactory getPresenterFactory() {
    return this.presenterFactory;
  }

}
