package com.example.extension.view;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.vaadin.ui.VerticalLayout;

public class ExtensionView extends VerticalLayout implements IExtensionView, IUiBindable {

  @UiField
  ButtonBar buttonBar;
  
  @Override
  public IButtonBar getButtonBar() {
    return buttonBar;
  }
  
}
