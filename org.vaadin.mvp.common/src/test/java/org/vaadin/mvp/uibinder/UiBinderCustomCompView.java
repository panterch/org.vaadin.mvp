package org.vaadin.mvp.uibinder;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class UiBinderCustomCompView extends Panel implements IUiBindable {

  @UiField
  Button save;

  @UiField
  Label title;

  @UiField
  Label labelOne;

  @UiField
  Label labelTwo;

  @UiField
  CustomComp customComp;

}
