package org.vaadin.mvp.uibinder.handler;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Locale;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.vaadin.mvp.uibinder.UiConstraintException;
import org.vaadin.mvp.uibinder.handler.ComponentHandler;

import com.vaadin.ui.VerticalLayout;

public class ComponentHandlerTest {

  private ComponentHandler instance;
  private VerticalLayout bean;

  @Before
  public void setUp() throws Exception {
    instance = new ComponentHandler(null, null, Locale.getDefault());
    bean = new VerticalLayout();
  }

  @Test
  public void testFindApplicableSetterMethodStringArg() throws SecurityException, NoSuchMethodException {
    Method m = instance.findApplicableSetterMethod(bean, "height", "67%");
    assertEquals("wrong setter selected", VerticalLayout.class.getMethod("setHeight", String.class), m);
  }

//  @Test
//  public void testFindApplicableSetterMethodFloatArg() throws SecurityException, NoSuchMethodException {
//    Method m = instance.findApplicableSetterMethod(bean, "height", 67.0f);
//    assertEquals("wrong setter selected", VerticalLayout.class.getMethod("setHeight", float.class), m);
//  }

  @Ignore("Currently not supported, introduce expressions, e.g. MVEL to support this.")
  @Test
  public void testFindApplicableSetterMethodFloatIntArg() throws SecurityException, NoSuchMethodException {
    Method m = instance.findApplicableSetterMethod(bean, "height", "67, #UNITS_PERCENTAGE");
    // FIXME: handle this case.
    assertEquals("wrong setter selected", VerticalLayout.class.getMethod("setHeight", float.class, int.class), m);
  }

  @Test
  public void testFindApplicableSetterMethodNoArg() throws SecurityException, NoSuchMethodException {
    Method m = instance.findApplicableSetterMethod(bean, "sizeFull", "");
    assertEquals("wrong setter selected", VerticalLayout.class.getMethod("setSizeFull"), m);
  }

  @Test
  public void testNamespaceUriToPackageName() throws UiConstraintException {
    String pkg = instance.namespaceUriToPackageName("urn:import:org.vaadin.test");
    assertEquals("invalid conversion of namespace to package", "org.vaadin.test", pkg);
  }
  
  @Test
  public void testNamespaceUriToPackageNameHttp() throws UiConstraintException {
    String pkg = instance.namespaceUriToPackageName("http://vaadin.org/test/some/pkg");
    assertEquals("invalid conversion of namespace to package", "org.vaadin.test.some.pkg", pkg);
  }

}
