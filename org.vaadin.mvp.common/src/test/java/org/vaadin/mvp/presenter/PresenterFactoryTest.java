package org.vaadin.mvp.presenter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.mvp.eventbus.EventArgument;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.eventbus.StubPresenter;
import org.vaadin.mvp.presenter.IPresenterFactory;
import org.vaadin.mvp.presenter.PresenterFactory;


public class PresenterFactoryTest {

  private IPresenterFactory instance;

  @Before
  public void setUp() throws Exception {
    instance = new PresenterFactory(new EventBusManager(), Locale.getDefault());
  }

  @Test
  public void testLoadModule() {
    StubPresenter p = (StubPresenter) instance.createPresenter(StubPresenter.class);
    assertNotNull(p);
    assertNotNull(p.getView());
    assertNotNull(p.getEventBus());
  }
  
  @Test
  public void testLoadModuleEvents() {
    StubPresenter p = (StubPresenter) instance.createPresenter(StubPresenter.class);
    p.getEventBus().selectMenuEntry(new EventArgument(1l, "Locations"));
    assertTrue(p.isEventReceived());
  }

}
