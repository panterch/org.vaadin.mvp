package org.vaadin.mvp.uibinder;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.uibinder.annotation.UiField;
import org.vaadin.mvp.uibinder.event.IEventBinder;
import org.vaadin.mvp.uibinder.handler.BindingParserHandler;
import org.vaadin.mvp.uibinder.handler.ComponentHandler;
import org.vaadin.mvp.uibinder.handler.EventHandler;
import org.vaadin.mvp.uibinder.handler.I18nHandler;
import org.vaadin.mvp.uibinder.handler.MethodHandler;
import org.vaadin.mvp.uibinder.handler.ResourceHandler;
import org.vaadin.mvp.uibinder.handler.UiHandler;
import org.vaadin.mvp.uibinder.resource.EmptyUiMessageSource;

import com.vaadin.ui.Component;

public class UiBinder {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(UiBinder.class);

  /** SAX Parser Factory to create UiParsers */
  private SAXParserFactory spf = SAXParserFactory.newInstance();

  /**
   * Ui message source to translate labels, texts, etc.
   */
  private IUiMessageSource messageSource = new EmptyUiMessageSource();

  /**
   * Constructor.
   */
  public UiBinder() {
    spf.setNamespaceAware(true);
    spf.setValidating(false);
  }

  /**
   * Set the message source to get translations.
   * 
   * @param messageSource
   *          a message source
   */
  public void setUiMessageSource(IUiMessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Checks if a view class given is bindable (i.e. implments
   * {@link IUiBindable} interface)
   * 
   * @param viewClass
   * @return
   */
  public boolean isBindable(Class<?> viewClass) {
    boolean b = IUiBindable.class.isAssignableFrom(viewClass);
    logger.debug("isBindable: Class<{}> = {}", viewClass.getName(), b);
    return b;
  }

  /**
   * Checks if a view with given name is bindable (i.e. XML ui definition
   * exists)
   * 
   * @param viewName
   * @return
   */
  public boolean isBindable(String viewName) {
    Class<? extends Component> viewClass = getViewClass(viewName);
    if (viewClass != null) {
      return isBindable(viewClass);
    }
    // no view class; check if a template is available
    InputStream resource = findUiResource(viewName);
    boolean b = resource != null;
    logger.debug("isBindable: {} = {}", viewName, b);
    return b;
  }

  /**
   * Try to resolve a view class and return that class; otherwise returns
   * <code>null</code>.
   * 
   * @param viewName
   *          Qualified view name.
   * @return
   */
  private Class<? extends Component> getViewClass(String viewName) {
    try {
      Class<? extends Component> viewClass = (Class<? extends Component>) Class.forName(viewName);
      return viewClass;
    } catch (ClassNotFoundException e) {
    }
    return null;
  }

  /**
   * Create a view instance binding a UI XML definition to a component tree. If
   * a non-null <code>eventBinder</code> is given, it is used to wire events
   * declared in the <code>event</code> namespace, e.g.
   * <code>&lt;v:Button e:click="targetEventName" ... /&gt;</code>.
   * 
   * @param <T>
   *          Type of the view component
   * @param viewClass
   *          Class of the view component
   * @param locale
   *          Locale used for translation
   * @param eventBinder
   *          Event binder that wires events declared in the UI XML
   * @return
   * @throws UiBinderException
   */
  public <T extends Component> T bind(Class<T> viewClass, Locale locale, IEventBinder eventBinder) throws UiBinderException {
    try {
      T view = (T) viewClass.newInstance();
      view = bind(viewClass.getName(), view, locale, eventBinder);
      return (T) view;
    } catch (InstantiationException e) {
      throw new UiConstraintException("Failed to instantiate component type: " + viewClass.getName());
    } catch (IllegalAccessException e) {
      throw new UiConstraintException("Failed to instantiate component type: " + viewClass.getName());
    }
  }

  /**
   * Create a view instance binding a UI XML definition to a component tree. If
   * a non-null <code>eventBinder</code> is given, it is used to wire events
   * declared in the <code>event</code> namespace, e.g.
   * <code>&lt;v:Button e:click="targetEventName" ... /&gt;</code>.
   * 
   * @param viewName
   *          Name of the view.
   * @param locale
   *          Locale used for translation
   * @param eventBinder
   *          Event binder that wires events declared in the UI XML
   * @return
   * @throws UiBinderException
   */
  public Component bind(String viewName, Locale locale, IEventBinder eventBinder) throws UiBinderException {
    Class<? extends Component> viewClass = getViewClass(viewName);
    if (viewClass != null) {
      return bind(viewClass, locale, eventBinder);
    }
    // use a standard class VerticalLayout
    Composite composite = bind(viewName, new Composite(), locale, eventBinder);
    if (composite.getComponentCount() == 1) {
      return composite.getComponent(0);
    }
    return composite;
  }

  /**
   * Create a view instance using given <code>view</code> as component tree
   * root. If a non-null <code>eventBinder</code> is given, it is used to wire
   * events declared in the <code>event</code> namespace, e.g.
   * <code>&lt;v:Button e:click="targetEventName" ... /&gt;</code>.
   * 
   * @param <T>
   *          Type of the view component
   * @param viewName
   *          Name of the view
   * @param view
   *          Instance of the view component
   * @param locale
   *          Locale used for translation
   * @param eventBinder
   *          Event binder that wires events declared in the UI XML
   * @return
   * @throws UiBinderException
   */
  private <T extends Component> T bind(String viewName, T view, Locale locale, IEventBinder eventBinder) throws UiBinderException {
    logger.info("Binding view: {}", viewName);
    InputStream resource = findUiResource(viewName);
    SAXParser parser;
    try {
      parser = spf.newSAXParser();
    } catch (Exception e) {
      throw new UiBinderException("Failed to create/configure SAX Parser to parse view", e);
    }

    // setup handlers
    ComponentHandler ch = new ComponentHandler(this, view, locale);
    UiHandler ui = new UiHandler(ch);

    I18nHandler lh = new I18nHandler(ch, messageSource, locale);

    ResourceHandler rh = new ResourceHandler(ch);
    EventHandler eh = new EventHandler(ch, eventBinder);
    ch.setEventBinder(eventBinder);

    MethodHandler mh = new MethodHandler(ch);
    
    BindingParserHandler dh = new BindingParserHandler(ch, lh, ui, rh, eh, mh);
    try {
      parser.parse(resource, dh);
    } catch (Exception e) {
      throw new UiBinderException("Failed to parse view [" + viewName + "]", e);
    }

    bindComponents(view, ui);
    
    if (view instanceof IUiInitializable) {
      ((IUiInitializable) view).init();
    }
    
    return view;
  }

  /**
   * Find the resource by the view name.
   * 
   * @param viewName
   * @return
   */
  private InputStream findUiResource(String viewName) {
    String viewDefinitionFile = viewName.replace('.', '/') + ".xml";
    logger.info("Lookup of XML UI resource: {}", viewDefinitionFile);
    InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream(viewDefinitionFile);
    return resource;
  }

  /**
   * Binds components of the view to fields marked with @UiField annotation.
   * 
   * @param view
   * @param uiHandler
   * @throws UiConstraintException
   */
  private void bindComponents(Component view, UiHandler uiHandler) throws UiConstraintException {

    // bind components with explicit binding to the view instance
    Map<String, Component> comps = uiHandler.getBoundComponents();
    Field[] fields = view.getClass().getDeclaredFields();
    for (Field field : fields) {
      // check if the field is bound anyway
      UiField uiField = field.getAnnotation(UiField.class);
      if (uiField == null) {
        continue;
      }
      // let's see if we've got a component for the field
      String name = field.getName();
      if (!comps.containsKey(name)) {
        continue;
      }
      Component component = comps.get(name);
      Class<?> fieldType = field.getType();
      boolean assignable = fieldType.isAssignableFrom(component.getClass());
      if (!assignable) {
        throw new UiConstraintException("The bound component '" + component + "' can not be assigned to the UiField of type '" + field.getName() + "' of '"
            + fieldType.getName() + "'");
      }
      // try to access the field and inject the component
      boolean accessible = field.isAccessible();
      if (!accessible) {
        field.setAccessible(true);
      }
      try {
        field.set(view, component);
      } catch (IllegalArgumentException e) {
        throw new UiConstraintException("The bound component '" + component + "' can't be set to field '" + field.getName() + "'", e);
      } catch (IllegalAccessException e) {
        throw new UiConstraintException("The bound component '" + component + "' can't be set to field '" + field.getName() + "'", e);
      }
      // reset accessibility
      field.setAccessible(accessible);
    }
  }

}
