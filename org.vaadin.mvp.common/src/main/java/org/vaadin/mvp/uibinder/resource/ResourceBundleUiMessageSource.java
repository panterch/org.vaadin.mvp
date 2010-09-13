package org.vaadin.mvp.uibinder.resource;

import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.print.attribute.HashAttributeSet;

import org.vaadin.mvp.uibinder.IUiMessageSource;


/**
 * Simple {@link ResourceBundle} based implementation of
 * {@link IUiMessageSource}.
 * 
 * @author tam
 */
public class ResourceBundleUiMessageSource implements IUiMessageSource {

  /** Map of resource bundles by locale */
  private transient Map<Locale, ResourceBundle> resourceBundles;

  /** ResourceBundle base name */
  private String baseName;

  /**
   * Constructor; takes a <code>baseName</code> of the message properties files.
   * 
   * @param baseName
   *          base name of message properties, e.g.
   *          <code>messages/Resources</code>
   */
  public ResourceBundleUiMessageSource(String baseName) {
    this.baseName = baseName;
    this.resourceBundles = new HashMap<Locale, ResourceBundle>();
  }

  @Override
  public String getMessage(String key, Locale locale) {
    ResourceBundle bundle = getBundle(locale);
    if (bundle.containsKey(key)) {
      return bundle.getString(key);
    }
    return "{{ message missing: " + key + "}}";
  }

  @Override
  public String getMessage(String key, Object[] args, Locale locale) {
    ResourceBundle bundle = getBundle(locale);
    if (bundle.containsKey(key)) {
      String template = bundle.getString(key);
      MessageFormat fmt = new MessageFormat(template, locale);
      StringBuffer messageBuffer = fmt.format(args, new StringBuffer(), new FieldPosition(0));
      return messageBuffer.toString();
    }
    return "{{ message missing: " + key + "}}";
  }

  /**
   * Lookup a bundle for the <code>locale</code>. If a resource bundle has been
   * loaded already, the "cached" instance is returned; otherwise the bundle is
   * loaded.
   * 
   * @param locale
   *          requested locale
   * @return A resource bundle for the locale
   */
  private ResourceBundle getBundle(Locale locale) {
    if (resourceBundles.containsKey(locale)) {
      return resourceBundles.get(locale);
    }
    // construct the resource bundle (synchronized to prevent multiple creation
    // of the same bundle
    synchronized (this) {
      if (resourceBundles.containsKey(locale)) {
        return resourceBundles.get(locale);
      }
      ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
      resourceBundles.put(locale, bundle);
      return bundle;
    }
  }

}
