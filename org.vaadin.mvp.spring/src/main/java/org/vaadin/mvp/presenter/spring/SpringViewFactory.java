package org.vaadin.mvp.presenter.spring;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.AbstractViewFactory;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.IViewFactory;
import org.vaadin.mvp.presenter.ViewFactoryException;
import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.UiBinder;


/**
 * Spring view factory creates view instances using delegation.
 * 
 * <p>
 * First, a specific view factory is looked up from the spring application
 * context: if a bean of type {@link IViewFactory} with name
 * <code><i>ViewName</i>ViewFactory</code> is present, then view creation is
 * delegated to it.
 * </p>
 * 
 * <p>
 * Second, a predefined autowired {@link SpringUiBinderViewFactory} is queried
 * if it's capable to bind the view. The {@link SpringUiBinderViewFactory} is
 * actually a wrapper around {@link UiBinder} which uses an XML definition (named
 * after the view class. Note that the view class must implement a marker interface
 * {@link IUiBindable}.
 * </p>
 * 
 * <p>
 * If no view can be created by delegation to either a specific view factory then
 * simply an instance of view class is returend.
 * </p>
 * 
 * @author tam
 * 
 */
@Component("springViewFactory")
public class SpringViewFactory extends AbstractViewFactory implements ApplicationContextAware {

  private static final String VIEW_FACTORY_BEAN_SUFFIX = "ViewFactory";

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(SpringViewFactory.class);

  private ApplicationContext applicationContext;

  @Autowired(required = true)
  private SpringUiBinderViewFactory binderFactory;

  /**
   * Create a view by delegation as described in this class document.
   * 
   * @param <T>
   * @param ebm
   * @param presenter
   * @param viewType
   * @param locale
   * @return
   * @throws ViewFactoryException
   */
  @Override
  public <T> T createView(EventBusManager ebm, IPresenter presenter, Class<T> viewType,
      Locale locale) throws ViewFactoryException {
    // find a custom view factory for the view
    String viewName = viewType.getSimpleName();
    String viewFactoryName = viewName + VIEW_FACTORY_BEAN_SUFFIX;
    if (applicationContext.containsBean(viewFactoryName)) {
      logger.debug("Using specific view factory '{}'", viewFactoryName);
      IViewFactory viewFactory = applicationContext.getBean(viewName, IViewFactory.class);
      return viewFactory.createView(ebm, presenter, viewType, locale);
    }
    // check if UiBindable
    if (binderFactory.canCreateView(viewType)) {
      return binderFactory.createView(ebm, presenter, viewType, locale);
    }

    // just create a new instance of the view and return
    try {
      T view = viewType.newInstance();
      return view;
    } catch (Exception e) {
      throw new ViewFactoryException();
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

}
