package com.example.privatebus.greeting.view;

import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;

/**
 * @author : apalumbo
 */
public interface IGreetingView extends Layout {
  Label getMessageLabel();
}
