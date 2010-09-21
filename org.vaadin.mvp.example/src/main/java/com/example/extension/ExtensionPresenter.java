package com.example.extension;

import org.vaadin.mvp.presenter.FactoryPresenter;

import com.example.extension.view.IExtensionView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ExtensionPresenter extends FactoryPresenter<IExtensionView, ExtensionEventBus> {

  @Override
  public void bind() {
    this.view.getButtonBar().getNextButton().addListener(new ClickListener() {
      public void buttonClick(ClickEvent event) {
        // ...
      }
    });
    this.view.getButtonBar().getPreviousButton().addListener(new ClickListener() {
      public void buttonClick(ClickEvent event) {
        // ...
      }
    });
  }

}
