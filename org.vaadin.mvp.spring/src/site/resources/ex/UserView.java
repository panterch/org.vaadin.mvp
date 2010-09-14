package com.example.useradmin.view;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

public class UserView extends VerticalLayout implements Layout, IUiBindable, IUserView {

  @UiField
  Form userForm;

  @Override
  public Form getUserForm() {
    return userForm;
  }
}
