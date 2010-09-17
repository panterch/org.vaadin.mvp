package org.vaadin.mvp.uibinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.uibinder.UiBinder;
import org.vaadin.mvp.uibinder.UiBinderException;
import org.vaadin.mvp.uibinder.resource.ResourceBundleUiMessageSource;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class UiBinderTest {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(UiBinderTest.class);

  /** Instance under test */
  private UiBinder instance;

  @Before
  public void setUp() throws Exception {
    instance = new UiBinder();
    // setup the message source
    ResourceBundleUiMessageSource ms = new ResourceBundleUiMessageSource("i18n/Resources");
    instance.setUiMessageSource(ms);
  }

  /**
   * Test how some components behave.
   */
  @Test
  public void testBuildWithPanel() {
    UiBinderView root = new UiBinderView();
    VerticalLayout layout = new VerticalLayout();
    root.setContent(layout);
    layout.setHeight("67%");
    ComponentContainer content = root.getContent();
    logger.info("content: {}", content);
    float height = layout.getHeight();
    int heightUnits = layout.getHeightUnits();
    logger.info("Height: {} unit: {}", height, heightUnits);
  }

  /**
   * Test that component tree is correctly built.
   * 
   * @throws UiBinderException
   */
  @Test
  public void testBindSimpleComponents() throws UiBinderException {
    UiBinderView root = instance.bind(UiBinderView.class, new Locale("en"), null);

    assertNotNull("component is null", root);
    boolean isPanel = root instanceof Panel;
    assertTrue("not a panel", isPanel);
    Panel panel = (Panel) root;
    Component layout = panel.getContent();
    assertTrue("first nested component should be a vertical layout", (layout instanceof VerticalLayout));
    VerticalLayout verticalLayout = (VerticalLayout) layout;
    Component label = verticalLayout.getComponent(0);
    assertNotNull("label is null", label);
    assertTrue("not a label: " + label.getClass(), (label instanceof Label));
  }

  /**
   * Test that attributes are set correctly on components.
   * 
   * @throws Exception
   */
  @Test
  public void testBindAttributes() throws Exception {
    UiBinderView root = instance.bind(UiBinderView.class, new Locale("en"), null);
    ComponentContainer content = root.getContent();
    assertNotNull("content should be a layout", content);
    VerticalLayout layout = (VerticalLayout) content;
    float height = layout.getHeight();
    assertEquals("height not set correctly", 67.0f, height, 0.0d);
    assertTrue("setSizeFull() has not been called", root.sizeFull);
  }

  /**
   * 
   * @throws Exception
   */
  @Test
  public void testBindEventListener() throws Exception {
    UiBinderView root = instance.bind(UiBinderView.class, new Locale("en"), null);
    assertNotNull("event button not bound", root.eventButton);
    assertTrue("click listener not set", root.eventButton.clickListenerSet);
  }

  /**
   * Test that custom components are bound correctly.
   * 
   * @see UiBinderCustomCompView.xml
   * 
   * @throws Exception
   */
  @Test
  public void testBindCustomComponents() throws Exception {
    UiBinderCustomCompView view = instance.bind(UiBinderCustomCompView.class, new Locale("en"), null);
    assertNotNull("custom comp has not been added to the view", view.customComp);
    assertNotNull("custom comp content not added", view.customComp.getContent());
    assertEquals("custom comp content is not a VerticalLayout", VerticalLayout.class, view.customComp.getContent().getClass());
    assertEquals("custom comp content is not a Label", Label.class, view.customComp.getContent().getComponentIterator().next().getClass());
    assertNotNull("custom comp field 'label' not bound to UiField", view.customComp.label);
    assertEquals("custom comp field 'label' not translated", "This is the view title", view.customComp.label.getCaption());
  }

  /**
   * Test that nested components are bound correctly.
   * 
   * @throws UiBinderException
   */
  @Test
  public void testBindNestedComponents() throws UiBinderException {

    // bind the view
    UiBinderView root = instance.bind(UiBinderView.class, new Locale("en"), null);

    assertNotNull("component is null", root);
    boolean isPanel = root instanceof Panel;
    assertTrue("not a panel", isPanel);
    Panel panel = (Panel) root;
    Component layout = panel.getContent();
    assertTrue("first nested component should be a vertical layout", (layout instanceof VerticalLayout));
    Component label = ((VerticalLayout) layout).getComponent(0);
    assertNotNull("label is null", label);
    assertTrue("not a label: " + label.getClass(), (label instanceof Label));
  }

  /**
   * Test that all fields with @UiField annotation are assigned with their
   * respective component.
   * 
   * @throws Exception
   */
  @Test
  public void testBindUiFields() throws Exception {
    UiBinderView view = instance.bind(UiBinderView.class, new Locale("en"), null);

    assertNotNull("button ui field not bound", view.save);
    assertNotNull("label ui field not bound", view.labelOne);
    assertNotNull("label ui field not bound", view.labelTwo);
  }

  /**
   * Test that translations are correctly handled
   * 
   * @throws Exception
   */
  @Test
  public void testBindTranslations() throws Exception {
    // bind the view
    UiBinderView view = instance.bind(UiBinderView.class, new Locale("en"), null);
    Label l = view.title;
    String caption = l.getCaption();
    assertNotNull("caption is null", caption);
    assertEquals("wrong caption", "This is the view title", caption);

    assertEquals("de only message wrong", "Dieser Text steht nur in deutsch", view.labelTwo.getCaption());
  }

  @Test
  public void testBindSimpleComponentsInitialized() throws UiBinderException {
    UiBinderViewInit root = instance.bind(UiBinderViewInit.class, new Locale("en"), null);
    assertNotNull("component is null", root);
    assertTrue("component is not initialzed", root.initialized);
  }  
  
  /**
   * Test that a concrete view works as well.
   * 
   * @throws Exception
   */
  /*
   * @Test public void testPointOfSaleView() throws Exception { PointOfSaleView
   * view = instance.bind(PointOfSaleView.class, new Locale("en"), null);
   * 
   * ComponentContainer content = view.getContent();
   * assertTrue("not a vertical layout", (content instanceof VerticalLayout)); }
   * 
   * public InputStream getResource(Class<?> viewClass) { String name =
   * viewClass.getSimpleName() + ".xml"; Package pkg = viewClass.getPackage();
   * String path = pkg.getName().replace('.', '/'); String resource = path + "/"
   * + name; logger.info("Loading view definition from: {}", resource);
   * InputStream is =
   * Thread.currentThread().getContextClassLoader().getResourceAsStream
   * (resource); return is; }
   */

}
