package com.example.useradmin.view;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class UserAdminView extends VerticalLayout implements IUserAdminView, IUiBindable {
  
  @UiField
  Table userList;

  @Override
  public Table getUserList() {
    return userList;
  }
  
}
