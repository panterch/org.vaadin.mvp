package com.example.privatebus.greeting;

import com.example.privatebus.greeting.view.GreetingView;
import com.example.privatebus.greeting.view.IGreetingView;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

/**
 * @author : apalumbo
 */
@Presenter(view = GreetingView.class)
public class GreetingPresenter extends BasePresenter<IGreetingView, GreetingEventBus> {

  public void onMessage(String messageText) {

    IGreetingView view = getView();
    view.getMessageLabel().setCaption(messageText);

  }
}
