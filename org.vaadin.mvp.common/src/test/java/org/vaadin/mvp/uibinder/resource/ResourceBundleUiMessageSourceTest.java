package org.vaadin.mvp.uibinder.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class ResourceBundleUiMessageSourceTest {

  private ResourceBundleUiMessageSource instance;

  @Before
  public void setUp() throws Exception {
    instance = new ResourceBundleUiMessageSource("i18n/Resources");
  }

  @Test
  public void testGetMessageStringLocale() {
    String message = instance.getMessage("message.key", new Locale("de"));
    assertNotNull(message);
    assertEquals("Hier ein kleiner Text um die Sache zu erläutern.", message);
  }
  
  @Test
  public void testGetMessageStringLocaleDefault() {
    String message = instance.getMessage("message.default", new Locale("en"));
    assertNotNull(message);
    assertEquals("Dieser Text steht nur in deutsch", message);
  }

  @Test
  public void testGetMessageStringObjectArrayLocale() {
    String message = instance.getMessage("message.args", new Object[] {"PARAM"}, new Locale("de"));
    assertNotNull(message);
    assertEquals("Hier eine Nachricht mit Parametern... PARAM wurde eingegeben.", message);
  }

}
