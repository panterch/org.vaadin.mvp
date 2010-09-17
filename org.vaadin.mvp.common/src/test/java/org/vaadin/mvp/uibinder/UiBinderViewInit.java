package org.vaadin.mvp.uibinder;

import com.vaadin.ui.Panel;

public class UiBinderViewInit extends Panel implements IUiInitializable {
  boolean initialized = false;

  @Override
  public void init() {
    this.initialized = true;
  }
}
