package com.example.view;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

public class ExampleView2 extends VerticalLayout implements IUiBindable {
  
  @UiField
  Button button;
  
  public Button getButton() {
    return button;
  }
}
