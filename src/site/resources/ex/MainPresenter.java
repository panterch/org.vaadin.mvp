package com.example.main;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.mvp.BasePresenter;
import org.vaadin.mvp.mvp.IPresenter;
import org.vaadin.mvp.mvp.IPresenterFactory;
import org.vaadin.mvp.mvp.Presenter;
import com.example.main.view.IMainView;
import com.example.main.view.MainView;
import com.example.menu.MenuPresenter;
import com.example.mvp.ExampleApp;
import com.vaadin.ui.Component;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Presenter(view = MainView.class)
public class MainPresenter extends BasePresenter<IMainView, MainEventBus>{

  private ExampleApp application;
  
  private MenuPresenter menuPresenter;
  
  private IPresenter<?, ?extends EventBus> contentPresenter;
  
  public void onStart(ExampleApp app) {
    // keep a reference to the application instance
    this.application = app;
    
    // set the applications main windows (the view)
    this.application.setMainWindow((Window) this.view);
    
    // load the menu presenter
    IPresenterFactory pf = application.getPresenterFactory();
    this.menuPresenter = (MenuPresenter) pf.createPresenter(MenuPresenter.class);
    this.view.setMenu(this.menuPresenter.getView());
  }
  
  public void onOpenModule(Class<? extends BasePresenter<?, ? extends EventBus>> presenter) {
    // load the menu presenter
    IPresenterFactory pf = application.getPresenterFactory();
    this.contentPresenter = pf.createPresenter(presenter);
    this.view.setContent((Component) this.contentPresenter.getView());
  }
  
  public void onShowDialog(Window dialog) {
    this.application.getMainWindow().addWindow(dialog);
  }
  
  @Override
  public void bind() {
    VerticalLayout mainLayout = this.view.getMainLayout();
    SplitPanel layoutPanel = this.view.getSplitLayout();
    mainLayout.setExpandRatio(layoutPanel, 1.0f);
    layoutPanel.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
    layoutPanel.setSplitPosition(150, SplitPanel.UNITS_PIXELS);
  }
  
}
