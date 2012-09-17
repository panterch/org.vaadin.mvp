package com.example.privatebus.manager.view;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * @author: apalumbo
 */
public class ManagerView extends VerticalLayout implements IManagerView, IUiBindable {

  @UiField
  private Panel firstPresenterContainer;

  @UiField
  private Panel secondPresenterContainer;

  @Override
  public Panel getFirstPresenterContainer() {
    return firstPresenterContainer;
  }

  @Override
  public Panel getSecondPresenterContainer() {
    return secondPresenterContainer;
  }

}
