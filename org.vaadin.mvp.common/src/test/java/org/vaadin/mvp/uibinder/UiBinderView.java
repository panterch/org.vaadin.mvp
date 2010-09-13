package org.vaadin.mvp.uibinder;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class UiBinderView extends Panel implements IUiBindable {

  boolean sizeFull = false;
  
  @UiField
  Button save;
  
  @UiField
  EventTestButton eventButton;

  @UiField
  Label title;

  @UiField
  Label labelOne;

  @UiField
  Label labelTwo;
  
  @Override
  public void setSizeFull() {
    sizeFull = true;
    super.setSizeFull();
  }

}
