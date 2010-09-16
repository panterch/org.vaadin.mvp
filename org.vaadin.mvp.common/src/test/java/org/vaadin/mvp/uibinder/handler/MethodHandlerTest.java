package org.vaadin.mvp.uibinder.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.ui.VerticalLayout;

public class MethodHandlerTest {

  private ComponentHandler ch;
  private MethodHandler instance;

  @Before
  public void setUp() throws Exception {
    ch = new ComponentHandler(null, new VerticalLayout(), Locale.getDefault());
    instance = new MethodHandler(ch);
  }

  @Test
  public void getTargetNamespace() {
    assertEquals("urn:org.vaadin.mvp.uibinder.method", instance.getTargetNamespace());
  }

  @Test
  public void handleElementOpen() {
    instance.handleElementOpen("", "addComponent");

    Method m = ch.getCurrentMethod();
    assertNotNull(m);
    assertEquals("addComponent", m.getName());
    assertEquals(1, m.getParameterTypes().length);
  }

  @Test (expected = IllegalArgumentException.class)
  public void handleElementOpenFail() {
    instance.handleElementOpen("", "nirvanaMethod");
  }

  @Test
  public void findMethod() throws SecurityException, NoSuchMethodException {
    Method m = instance.findMethod(VerticalLayout.class, "addComponent");
    assertNotNull(m);
    assertEquals("addComponent", m.getName());
    assertEquals(1, m.getParameterTypes().length);
  }

  @Test
  public void findMethodNotFound() throws SecurityException, NoSuchMethodException {
    Method m = instance.findMethod(VerticalLayout.class, "nirvanaMethod");
    assertNull(m);
  }
}
