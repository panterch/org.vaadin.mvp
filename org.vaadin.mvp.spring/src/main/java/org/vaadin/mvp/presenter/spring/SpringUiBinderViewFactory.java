package org.vaadin.mvp.presenter.spring;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.UiBinderViewFactory;
import org.vaadin.mvp.presenter.ViewFactoryException;
import org.vaadin.mvp.uibinder.resource.spring.SpringUiMessageSource;


/**
 * Spring UI binder view factory.
 * 
 * @author tam
 */
@Component("uibinderViewFactory")
public class SpringUiBinderViewFactory extends UiBinderViewFactory {
  
  @Autowired
  private MessageSource messageSource;
  
  @PostConstruct
  public void postConstruct() {
    SpringUiMessageSource sms = new SpringUiMessageSource(messageSource);
    this.uiBinder.setUiMessageSource(sms);
  }

  @Override
  public <T> T createView(EventBusManager ebm, IPresenter presenter, Class<T> viewType, Locale locale) throws ViewFactoryException {
    return super.createView(ebm, presenter, viewType, locale);
  }

}
