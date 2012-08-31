package org.vaadin.mvp.presenter.spring;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.EventBusManager;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.spring.SpringPresenterFactory;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/testApplicationContext.xml")
public class SpringPresenterFactoryTest {

  @Autowired
  SpringPresenterFactory instance;

  @Before
  public void setUp() throws Exception {
    instance.setEventManager(new EventBusManager());
    instance.setLocale(Locale.getDefault());
  }

  @Test
  public void testFactoryPresent() {
    assertNotNull(instance);
  }

  @Test
  public void testCreate() {
    IPresenter<?, ? extends EventBus> presenter = instance.createPresenter("spring");
    assertNotNull("created presenter is null", presenter);
    assertNotNull("presenters view is null", presenter.getView());
    assertTrue("presenters bind() method has not been called", ((SpringPresenter)presenter).bound);
    assertNotNull("presenter eventbus is null", presenter.getEventBus());
  }

}
