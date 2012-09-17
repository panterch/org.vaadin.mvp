package com.example.privatebus.manager;

import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.IPresenterFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.example.mvp.ExampleApp;
import com.example.privatebus.greeting.GreetingEventBus;
import com.example.privatebus.greeting.GreetingPresenter;
import com.example.privatebus.manager.view.IManagerView;
import com.example.privatebus.manager.view.ManagerView;

/**
 * @author: apalumbo
 */
@Presenter(view = ManagerView.class)
public class ManagerPresenter extends FactoryPresenter<IManagerView, ManagerEventBus> {

  private GreetingPresenter firstGreetingPresenter;
  private GreetingPresenter secondGreetingPresenter;

  public void onGreetingFirst() {
    doSalutation(firstGreetingPresenter.getEventBus());
  }

  public void onGreetingSecond() {
    doSalutation(secondGreetingPresenter.getEventBus());
  }

  private void doSalutation(GreetingEventBus greetingEventBus) {
    String message = "Hello, the current server tick time is " + System.nanoTime();
    greetingEventBus.message(message);
  }

  @Override
  public void bind() {
    super.bind();

    IManagerView managerView = getView();

    // not really the best way to get the presenter factory, an Issue need to be opened as
    // in case of FactoryPresenter the factory aware interface need to be processed before calling
    // the bind event
    IPresenterFactory thePresenterFactory = ((ExampleApp) application).getPresenterFactory();

    firstGreetingPresenter = (GreetingPresenter) thePresenterFactory.createPresenter(GreetingPresenter.class);
    managerView.getFirstPresenterContainer().setContent(firstGreetingPresenter.getView());

    secondGreetingPresenter = (GreetingPresenter) thePresenterFactory.createPresenter(GreetingPresenter.class);
    managerView.getSecondPresenterContainer().setContent(secondGreetingPresenter.getView());

  }
}
