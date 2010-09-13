package org.vaadin.mvp.uibinder.resource;

import java.util.Locale;

import org.vaadin.mvp.uibinder.IUiMessageSource;


/**
 * An empty default UI message source returning just the input key.
 * 
 * @author tam
 */
public class EmptyUiMessageSource implements IUiMessageSource {

  @Override
  public String getMessage(String key, Locale locale) {
    return key + "_" + locale;
  }

  @Override
  public String getMessage(String key, Object[] args, Locale locale) {
    return key + "_" + locale;
  }

}
