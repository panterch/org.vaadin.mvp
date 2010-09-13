package org.vaadin.mvp.uibinder.resource.spring;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.vaadin.mvp.uibinder.IUiMessageSource;


/**
 * UiMessageSource backed with Spring MessageSource.
 * 
 * @author tam
 */
public class SpringUiMessageSource implements IUiMessageSource {
  
  /** Spring {@link MessageSource} */
  private MessageSource messageSource;
  
  public SpringUiMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public String getMessage(String key, Locale locale) {
    String def = "{{missing message: " + key + "}}";
    return this.messageSource.getMessage(key, new Object[0], def, locale);
  }

  @Override
  public String getMessage(String key, Object[] args, Locale locale) {
    String def = "{{missing message: " + key + "}}";
    return this.messageSource.getMessage(key, args, def, locale);
  }

}
