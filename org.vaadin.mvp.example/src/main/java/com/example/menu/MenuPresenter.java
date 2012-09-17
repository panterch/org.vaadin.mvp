package com.example.menu;

import com.example.privatebus.manager.ManagerPresenter;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.example.main.MainEventBus;
import com.example.menu.view.IMenuView;
import com.example.menu.view.MenuView;
import com.example.useradmin.UserAdminPresenter;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.Tree;

@Presenter(view = MenuView.class)
public class MenuPresenter extends BasePresenter<IMenuView, MainEventBus> {

  @Override
  public void bind() {
    // create and add user admin entry
    MenuEntry userAdminEntry = new MenuEntry("User administration", UserAdminPresenter.class);
    addItemToTree(userAdminEntry);

    // create and add private presenter entry
    MenuEntry privatePresenterEntry = new MenuEntry("Private presenter", ManagerPresenter.class);
    addItemToTree(privatePresenterEntry);
    
    // set initially selected menu entry
    this.view.getTree().setValue(userAdminEntry);
  }

  private void addItemToTree(MenuEntry userAdminEntry) {
    Tree tree = this.view.getTree();
    tree.addItem(userAdminEntry);
    tree.setChildrenAllowed(userAdminEntry, false);
  }

  public void onSelectMenu(ValueChangeEvent event) {
    // get the selected menu entry and initiate another event
    MenuEntry menuEntry = (MenuEntry) this.view.getTree().getValue();
    if(menuEntry == null) {
      return;
    }
    this.eventBus.openModule(menuEntry.getPresenterType());
  }

}
