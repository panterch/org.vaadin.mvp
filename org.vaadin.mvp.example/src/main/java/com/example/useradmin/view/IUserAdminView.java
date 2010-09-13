package com.example.useradmin.view;

import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;

public interface IUserAdminView extends Layout {

  public abstract Table getUserList();

}
