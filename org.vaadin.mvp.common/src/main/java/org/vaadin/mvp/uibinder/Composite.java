package org.vaadin.mvp.uibinder;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * A simple component used in UiBinder when the view to bind is not defined in
 * it's own class.
 * 
 * @author tam
 */
public class Composite extends VerticalLayout {
  
  private int count;

  public int getComponentCount() {
    return count;
  }
  
  @Override
  public void addComponent(Component c) {
    super.addComponent(c);
    count++;
  }
  
}
