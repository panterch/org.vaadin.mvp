package org.vaadin.mvp.presenter.spring;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;


@Component("spring")
@Scope("prototype")
@Presenter(view = SpringView.class)
public class SpringPresenter extends BasePresenter<SpringView, SpringEventBus> {
  
  protected boolean bound = false;

  @Override
  public void bind() {
    bound = true;
    super.bind();
  }
  
}
