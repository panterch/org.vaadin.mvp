package org.vaadin.mvp.presenter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.uibinder.IUiMessageSource;

import com.vaadin.Application;


/**
 * Abstract base class for presenter factories.
 * 
 * <p>
 * A Presenter Factory is stateful since it hold a reference to the
 * {@link EventBusManager}, an instance is required for each application (i.e.
 * session).
 * 
 * @author tam
 */
public abstract class AbstractPresenterFactory implements IPresenterFactory {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(AbstractPresenterFactory.class);

  /** Event bus manager instance */
  protected EventBusManager eventBusManager;
  
  protected IPresenterFactoryCustomizer customizer;
  
  /** The current locale */
  protected Locale locale = Locale.getDefault();
  
  /** Optional reference to the Application and MessageSource */
  protected Application application;
  protected IUiMessageSource messageSource;    
  
  public AbstractPresenterFactory() {
  }

  /**
   * Set the Event manager.
   * 
   * @param eventManager
   */
  public void setEventManager(EventBusManager eventManager) {
    this.eventBusManager = eventManager;
  }
  
  /**
   * Set the locale.
   * 
   * @param locale
   */
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  /**
   * Return the event manager.
   * @return
   */
  public EventBusManager getEventBusManager() {
    return eventBusManager;
  }

  /**
   * Return the locale.
   * @return
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * Set a customizer.
   * @param customizer
   */
  public void setCustomizer(IPresenterFactoryCustomizer customizer) {
    this.customizer = customizer;
  }

  @Override
  public IPresenter<?, ? extends EventBus> createPresenter(Object arg){
    IPresenter<?, ? extends EventBus> presenter = create(arg);
    if(customizer != null) {
      customizer.customize(presenter);
    }
    if(presenter instanceof IFactoryAwarePresenter) {
      ((IFactoryAwarePresenter) presenter).setPresenterFactory(this);
    }
    presenter.setApplication(application);
    presenter.setMessageSource(messageSource);    
    return presenter;
  }

  protected abstract IPresenter<?, ? extends EventBus> create(Object arg);
  
  /**
   * Utility method to create/register an event bus.
   * 
   * @param presenterClass
   * @param presenter
   * @return
   */
  protected EventBus createEventBus(Class<IPresenter> presenterClass, IPresenter presenter) {
    Type gsc = presenterClass.getGenericSuperclass();
    logger.debug("Generic super class: {}", gsc);
    ParameterizedType pt = (ParameterizedType) gsc;
    Type[] typeArgs = pt.getActualTypeArguments();
    for (Type type : typeArgs) {
      logger.debug("Type arg: {}", type);
    }

    EventBus eb = null;
    Type ebt = typeArgs[1];
    if (EventBus.class.isAssignableFrom((Class<?>) ebt)) {
      Class<? extends EventBus> eventBusType = (Class<? extends EventBus>) ebt;
      logger.debug("EventBus class: {}", eventBusType.getName());
      eb = this.eventBusManager.register(eventBusType, presenter);
    }
    return eb;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  public void setMessageSource(IUiMessageSource messageSource) {
    this.messageSource = messageSource;
  }

}