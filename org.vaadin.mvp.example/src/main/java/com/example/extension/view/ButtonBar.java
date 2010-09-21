package com.example.extension.view;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;

public class ButtonBar extends Panel implements IButtonBar, IUiBindable {


  @UiField
  private Button next;
  
  @UiField
  private Button previous;

  @Override
  public Button getNextButton() {
    return next;
  }

  @Override
  public Button getPreviousButton() {
    return previous;
  }
  
}
