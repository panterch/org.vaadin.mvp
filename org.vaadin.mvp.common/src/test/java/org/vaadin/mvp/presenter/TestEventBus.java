package org.vaadin.mvp.presenter;

import org.vaadin.mvp.eventbus.EventBus;

/**
 * @author: apalumbo
 */
public interface TestEventBus extends EventBus {

  public void testEventWithArgument(String theArgument);

  public void testEventWithoutArgument();

}
