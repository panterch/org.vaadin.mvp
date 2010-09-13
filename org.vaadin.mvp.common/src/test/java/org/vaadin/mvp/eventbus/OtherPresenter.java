package org.vaadin.mvp.eventbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.presenter.BasePresenter;


public class OtherPresenter extends BasePresenter<OtherView, StubEventBus> {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(OtherPresenter.class);
  
  boolean received = false;

  public void onGlobalEvent(String message) {
    logger.info("Got the global message: {}", message);
    received = true;
  }

}
