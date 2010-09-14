package com.example.useradmin;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;
import com.example.main.MainPresenter;
import com.vaadin.ui.Window;

public interface UserAdminEventBus extends EventBus {

  @Event(handlers = { UserAdminPresenter.class })
  public void createUser();

  @Event(handlers = { UserAdminPresenter.class })
  public void removeUser();

  @Event(handlers = { MainPresenter.class })
  public void showDialog(Window dialog);

  @Event(handlers = { UserAdminPresenter.class })
  public void saveUser();
  
  @Event(handlers = { UserAdminPresenter.class })
  public void cancelEditUser();

}
