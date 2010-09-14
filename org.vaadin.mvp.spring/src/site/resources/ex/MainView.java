package com.example.main.view;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;
import com.vaadin.ui.Component;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MainView extends Window implements IMainView, IUiBindable {

  @UiField
  VerticalLayout mainLayout;
  
  @UiField
  SplitPanel splitLayout;
  
  @Override
  public void setMenu(Component menu) {
    splitLayout.setFirstComponent(menu);
  }

  @Override
  public void setContent(Component content) {
    splitLayout.setSecondComponent(content);
  }

  @Override
  public SplitPanel getSplitLayout() {
    return splitLayout;
  }
  
  @Override
  public VerticalLayout getMainLayout() {
    return mainLayout;
  }
  
}
