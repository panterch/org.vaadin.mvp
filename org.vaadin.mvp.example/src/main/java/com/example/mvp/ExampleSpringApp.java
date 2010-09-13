package com.example.mvp;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.presenter.spring.SpringMvpApplication;

import com.example.spring.main.SpringMainPresenter;

@Component("springMvpApp")
@Scope("prototype")
public class ExampleSpringApp extends SpringMvpApplication {

  private SpringMainPresenter mainPresenter;

  @Override
  public void preInit() {
    this.presenterFactory.setLocale(getLocale());
  }

  @Override
  public void postInit() {
    mainPresenter = (SpringMainPresenter) presenterFactory.createPresenter("springMainPresenter");
    mainPresenter.getEventBus().start(this);
  }

}
