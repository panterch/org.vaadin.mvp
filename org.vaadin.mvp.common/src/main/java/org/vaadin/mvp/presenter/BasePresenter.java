package org.vaadin.mvp.presenter;

import java.util.Locale;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.uibinder.IUiMessageSource;

import com.vaadin.Application;

/**
 * Abstract base class for presenters.
 * 
 * @author tam
 *
 * @param <V> Type of the view for this presenter
 * @param <E> Type of the event bus used by the presenter
 */
public abstract class BasePresenter<V, E extends EventBus> implements IPresenter<V, E> {

  private boolean bound = false;

  protected E eventBus = null;
  protected V view = null;

  /** Optional reference to the Application and MessageSource */  
  protected Application application;
  protected IUiMessageSource messageSource;  
  
  @Override
  public void setEventBus(E eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public E getEventBus() {
    return this.eventBus;
  }

  @Override
  public void setView(V view) {
    this.view = view;
  }

  @Override
  public V getView() {
    return this.view;
  }

  @Override
  public void bind() {
    // override in implementation
  }

  @Override
  public void bindIfNeeded() {
    if (!bound) {
      this.bind();
      this.bound = true;
    }
  }

  @Override
  public Application getApplication() {
    return application;
  }

  @Override
  public void setApplication(Application application) {
    this.application = application;
  }
  
  @Override
  public IUiMessageSource getMessageSource() {
    return messageSource;
  }

  @Override
  public void setMessageSource(IUiMessageSource messageSource) {
    this.messageSource = messageSource;
  }


  /**
   * Returns the message for argument <code>key</code> or a default indicating
   * message is missing.
   * 
   * @param key
   *          Message key
   * @return Message translation in current locale
   */
  @Override  
  public String getMessage(String key, Locale locale) {
    return getMessage(key, locale, (Object[]) null);
  }

  /**
   * Returns the message for argument <code>key</code> parameterized with
   * <code>args</code> or a default indicating message is missing.
   * 
   * @param key
   *          Message key
   * @param args
   *          Message parameters
   * @return Message translation in current locale
   */
  @Override  
  public String getMessage(String key, Locale locale, Object... args) {
    return messageSource.getMessage(key, args, locale);
  }
  
  @Override
  public void showNotification(String caption) {
    application.getMainWindow().showNotification(caption);
  }
  
  @Override
  public void showNotification(String caption, String description, int type) {
    application.getMainWindow().showNotification(caption, description, type);
  }  
}
