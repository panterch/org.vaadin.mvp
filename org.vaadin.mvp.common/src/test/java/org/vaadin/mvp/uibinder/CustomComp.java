package org.vaadin.mvp.uibinder;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

/**
 * Custom Component used in test.
 * 
 * @author tam
 */
public class CustomComp extends Panel implements IUiBindable {

  @UiField Label label;
  
}
