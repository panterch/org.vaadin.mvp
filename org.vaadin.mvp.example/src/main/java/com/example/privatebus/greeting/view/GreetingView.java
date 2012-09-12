package com.example.privatebus.greeting.view;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

/**
 * Created by IntelliJ IDEA.
 * User: apalumbo
 * Date: 9/12/12
 * Time: 1:41 AM
 */
public class GreetingView extends VerticalLayout implements IGreetingView, IUiBindable {

  @UiField
  private Label messageLabel;

  @Override
  public Label getMessageLabel() {
    return messageLabel;
  }
}
