package org.vaadin.mvp.uibinder.handler;

import java.util.Locale;

import org.vaadin.mvp.uibinder.IUiMessageSource;



public class I18nHandler implements TargetHandler {
  
  private Locale locale;
  
  private IUiMessageSource ms;
  
  private ComponentHandler ch; // delegate

  /**
   * 
   * @param ch
   * @param locale
   * @param rb
   */
  public I18nHandler(ComponentHandler ch, IUiMessageSource ms, Locale locale) {
    this.ms = ms;
    this.ch = ch;
    this.locale = locale;
  }

  @Override
  public String getTargetNamespace() {
    return "urn:org.vaadin.mvp.uibinder.message";
  }

  @Override
  public void handleElementOpen(String uri, String name) {
    // not supported, event is just ignored
  }
  
  @Override
  public void handleElementClose() {
    // not supported, event is just ignored
  }

  @Override
  public void handleAttribute(String name, Object value) {
    String message = ms.getMessage(value.toString(), locale);
    ch.handleAttribute(name, message);
  }

}
