package com.example.menu;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;


public class MenuEntry {

  private String caption;
  private Class<? extends BasePresenter<?, ? extends EventBus>> presenterType;
  
  public MenuEntry(String caption, Class<? extends BasePresenter<?, ? extends EventBus>> presenterType) {
    this.caption = caption;
    this.presenterType = presenterType;
  }

  public String getCaption() {
    return caption;
  }

  public Class<? extends BasePresenter<?, ? extends EventBus>> getPresenterType() {
    return presenterType;
  }
  
  public String toString() {
    return caption;
  }
  
}
