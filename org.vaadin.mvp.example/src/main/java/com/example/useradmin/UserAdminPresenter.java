package com.example.useradmin;

import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.ViewFactoryException;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.example.useradmin.view.IUserAdminView;
import com.example.useradmin.view.UserAdminView;
import com.example.useradmin.view.UserView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Form;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

@Presenter(view = UserAdminView.class)
public class UserAdminPresenter extends FactoryPresenter<IUserAdminView, UserAdminEventBus> {

  private BeanItemContainer<User> container;
  
  private Window dialog = null;
  private Form userForm = null;

  @Override
  public void bind() {
    Table userList = this.view.getUserList();
    container = new BeanItemContainer<User>(User.class);
    userList.setContainerDataSource(container);
    userList.setVisibleColumns(new String[]{"userName", "firstName", "lastName"});
    userList.setImmediate(true);
    userList.setSelectable(true);
    userList.setMultiSelect(false);
    // userList.setEditable(true);
  }

  /* first version 
  public void onCreateUser() {
    // create an user
    User u = new User();
    u.setUserName("newuser");
    u.setFirstName("First name");
    u.setLastName("Last name");
    this.container.addBean(u);
  }
  */
  
  /* second version, using popup */
  public void onCreateUser() throws ViewFactoryException {
    // create view
    UserView view = this.createView(UserView.class);
    
    // configure the form with bean item
    this.userForm = view.getUserForm();
    User u = new User();
    u.setUserName("newuser");
    u.setFirstName("First name");
    u.setLastName("Last name");
    BeanItem<User> beanItem = new BeanItem<User>(u);
    this.userForm.setItemDataSource(beanItem);
    
    // create a window using caption from view
    this.dialog = new Window(view.getCaption());
    view.setCaption(null);
    this.dialog.setModal(true);
    dialog.addComponent(view);
    dialog.setWidth("300px");
    this.eventBus.showDialog(this.dialog);
  }

  public void onRemoveUser() {
    // check if a user is selected in the table
    Table userList = this.view.getUserList();
    Object selected = userList.getValue();
    if (selected != null) {
      this.container.removeItem(selected);
    }
  }
  
  public void onSaveUser() {
    // get the user and add it to the container
    BeanItem<User> item = (BeanItem<User>) this.userForm.getItemDataSource();
    User u = item.getBean();
    this.container.addBean(u);
    
    // close dialog
    this.closeDialog();
  }
  
  public void onCancelEditUser() {
    // close dialog only
    this.closeDialog();
  }
  
  private void closeDialog() {
    // dismiss the dialog
    Window applicationWindow = (Window) this.dialog.getParent();
    applicationWindow.removeWindow(this.dialog);
    this.dialog = null;
    this.userForm = null;
  }

}
