package org.vaadin.mvp.presenter.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.AbstractPresenterFactory;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.IViewFactory;
import org.vaadin.mvp.presenter.PresenterFactory;
import org.vaadin.mvp.presenter.ViewFactoryException;
import org.vaadin.mvp.presenter.annotation.Presenter;


/**
 * Spring based {@link PresenterFactory}.
 * 
 * @author tam
 * 
 */
@Component("springPresenterFactory")
@Scope("prototype")
public class SpringPresenterFactory extends AbstractPresenterFactory implements ApplicationContextAware {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(SpringPresenterFactory.class);

  /** Spring application context. */
  private ApplicationContext applicationContext;

  @Autowired(required = true)
  private SpringViewFactory viewFactory;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @SuppressWarnings("unchecked")
  public IPresenter<?, ? extends EventBus> create(Object name,EventBus parentEventBus) {
    if (!(name instanceof String)) {
      throw new IllegalArgumentException("Argument is expected to be a bean name (string)");
    }
    String beanName = (String) name;
    if (applicationContext.containsBean(beanName)) {
      IPresenter p = applicationContext.getBean(beanName, IPresenter.class);
      p.setApplication(application);
      p.setMessageSource(messageSource);   
      Presenter def = p.getClass().getAnnotation(Presenter.class);
      if (def == null) {
        throw new IllegalArgumentException("Missing @Presenter annotation on bean '" + beanName + "'");
      }

      EventBus eventBus = createEventBus((Class<IPresenter>) p.getClass(), p,parentEventBus);
      p.setEventBus(eventBus);

      try {
        Object view = viewFactory.createView(eventBusManager, p, def.view(), locale);
        p.setView(view);
      } catch (ViewFactoryException e) {
        logger.error("Failed to create view for presenter", e);
      }
      
      p.bind();

      return p;
    }
    throw new IllegalArgumentException("No presenter is defined for name: " + name);
  }

  @Override
  public IViewFactory getViewFactory() {
    return this.viewFactory;
  }

}
