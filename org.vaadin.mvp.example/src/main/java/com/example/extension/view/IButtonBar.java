package com.example.extension.view;

import org.vaadin.mvp.uibinder.annotation.UiField;

import com.vaadin.ui.Button;

public interface IButtonBar {

  public abstract Button getNextButton();
  
  public abstract Button getPreviousButton();
  
}
