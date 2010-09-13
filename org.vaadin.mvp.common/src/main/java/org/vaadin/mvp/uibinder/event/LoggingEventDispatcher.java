package org.vaadin.mvp.uibinder.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event dispatcher that only logs info messages for events being received.
 * 
 * @author tam
 * 
 */
public class LoggingEventDispatcher implements IEventDispatcher {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(LoggingEventDispatcher.class);

  @Override
  public void dispatch(String name, Object event) {
    logger.info("Event received: {}, {}", name, event);
  }

  @Override
  public void dispatch(String name, Object... args) {
    logger.info("Event received: {}, {}", name, args);
  }

}
