package org.vaadin.mvp.presenter;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.uibinder.UiBinder;
import org.vaadin.mvp.uibinder.UiBinderException;
import org.vaadin.mvp.uibinder.event.EventDispatcherBinder;

import com.vaadin.ui.Component;

public class UiBinderViewFactory extends AbstractViewFactory {

  
  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(UiBinderViewFactory.class);
  
  protected UiBinder uiBinder = new UiBinder();

  @Override
  public <T> T createView(EventBusManager ebm, IPresenter presenter, Class<T> viewType, Locale locale) throws ViewFactoryException {
    // use UI binder to create the view
    Component view;
    try {
      view = createBinderView(ebm, (Class<? extends Component>) viewType, presenter, locale);
      return (T) view;
    } catch (UiBinderException e) {
      throw new ViewFactoryException("Failed to create view", e);
    }
  }

  @Override
  public boolean canCreateView(Class<?> viewType) {
    return uiBinder.isBindable(viewType);
  }
  
  /**
   * 
   * @param viewClass
   * @param presenter
   * @return
   * @throws UiBinderException
   */
  protected Component createBinderView(EventBusManager emb, Class<? extends Component> viewClass,
      IPresenter<?, ? extends EventBus> presenter, Locale locale) throws UiBinderException {

    logger.info("Creating UiBinder view for {}", viewClass.getName());

    EventBusDispatcher dispatcher = new EventBusDispatcher(emb, presenter.getEventBus());
    EventDispatcherBinder eventBinder = new EventDispatcherBinder(dispatcher);

    return uiBinder.bind(viewClass, locale, eventBinder);
  }

  public UiBinder getUiBinder() {
    return uiBinder;
  }

  public void setUiBinder(UiBinder uiBinder) {
    this.uiBinder = uiBinder;
  }

}
