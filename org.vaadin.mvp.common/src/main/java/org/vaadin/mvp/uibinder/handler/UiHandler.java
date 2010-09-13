package org.vaadin.mvp.uibinder.handler;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.mvp.uibinder.UiConstraintException;

import com.vaadin.ui.Component;

/**
 * Handler to inject named ui components to the view root instance.
 * 
 * @author tam
 * 
 */
public class UiHandler implements TargetHandler {

  private Map<String, Component> boundComponents = new HashMap<String, Component>();

  private ComponentHandler ch;

  public UiHandler(ComponentHandler ch) {
    this.ch = ch;
  }

  @Override
  public String getTargetNamespace() {
    return "urn:org.vaadin.mvp.uibinder";
  }

  @Override
  public void handleElementOpen(String uri, String name) {
    // not supported, i.e. currently not used
  }

  @Override
  public void handleElementClose() {
    // not supported, i.e. currently not used
  }

  @Override
  public void handleAttribute(String name, Object value) throws UiConstraintException {
    if ("field".equals(name)) {
      if (boundComponents.containsKey(value)) {
        throw new UiConstraintException("Duplicate field binding for name: " + value);
      }
      Component current = ch.getCurrent();
      this.boundComponents.put(value.toString(), current);
    }
  }

  public Map<String, Component> getBoundComponents() {
    return boundComponents;
  }

}
