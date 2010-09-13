package org.vaadin.mvp.uibinder;

import com.vaadin.ui.Button;

public class EventTestButton extends Button {

  boolean clickListenerSet = false;
  
  @Override
  public void addListener(ClickListener listener) {
    clickListenerSet = true;
    super.addListener(listener);
  }
  
}
