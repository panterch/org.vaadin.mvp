package com.example.menu.view;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

public class MenuView extends VerticalLayout implements IMenuView, IUiBindable {

  @UiField
  Tree menuTree;

  @Override
  public Tree getTree() {
    return this.menuTree;
  }
  
}
