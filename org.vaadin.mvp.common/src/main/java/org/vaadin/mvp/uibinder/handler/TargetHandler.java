package org.vaadin.mvp.uibinder.handler;

import org.vaadin.mvp.uibinder.UiBinderException;
import org.vaadin.mvp.uibinder.UiConstraintException;



public interface TargetHandler {

  public String getTargetNamespace();

  public void handleElementOpen(String uri, String name) throws UiBinderException;
  
  public void handleElementClose() throws UiBinderException;
  
  public void handleAttribute(String name, Object value) throws UiConstraintException;
  
}
