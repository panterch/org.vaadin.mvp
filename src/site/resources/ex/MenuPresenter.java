package com.example.menu;

import org.vaadin.mvp.mvp.BasePresenter;
import org.vaadin.mvp.mvp.Presenter;
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
    // create an entry
    MenuEntry userAdminEntry = new MenuEntry("User administration", UserAdminPresenter.class);
    
    // add the entry to the tree
    Tree tree = this.view.getTree();
    tree.addItem(userAdminEntry);
    tree.setChildrenAllowed(userAdminEntry, false);
  }
  
  public void onSelectMenu(ValueChangeEvent event) {
    // get the selected menu entry and initiate another event
    MenuEntry menuEntry = (MenuEntry) this.view.getTree().getValue();
    this.eventBus.openModule(menuEntry.getPresenterType());
  }

}
