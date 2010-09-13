package org.vaadin.mvp.eventbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;


/**
 * Stub implementation of a presenter; a mock is not suitable since we have no
 * interfaces of presenters, but have to provide an exact method.
 * 
 * @author tam
 */
@Presenter(view = StubView.class)
public class StubPresenter extends BasePresenter<StubView, StubEventBus> {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(StubPresenter.class);

  boolean eventReceived = false;

  public void onSelectMenuEntry(EventArgument dto) {
    logger.info("Got the event!");
    eventReceived = true;
  }

  public void onGlobalEvent(String message) {
    logger.info("Global event: {}", message);
    eventReceived = true;
  }

  public boolean isEventReceived() {
    return eventReceived;
  }

}
