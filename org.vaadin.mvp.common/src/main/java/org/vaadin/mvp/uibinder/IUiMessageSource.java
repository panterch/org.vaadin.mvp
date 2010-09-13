package org.vaadin.mvp.uibinder;

import java.util.Locale;
import java.util.ResourceBundle;

import org.vaadin.mvp.uibinder.resource.ResourceBundleUiMessageSource;


/**
 * Interface for Message Sources to use when translating UI labels, texts, etc.
 * 
 * <p>
 * For a simple implementation based on {@link ResourceBundle} see
 * {@link ResourceBundleUiMessageSource}.
 * </p>
 * 
 * <p>
 * Other implementations may wrap third party solutions (e.g. Spring Framework
 * <a href=
 * "http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/context/MessageSource.html"
 * > MessageSource</a>. However such wrappers are not provided in order to keep
 * dependencies minimal.
 * </p>
 * 
 * @author tam
 * 
 */
public interface IUiMessageSource {

  /**
   * Return the message for <code>key</code> in <code>locale</code> specified.
   * If no message is defined for the given <code>key</code> the key itself is
   * returned potentially marked as missing (e.g.
   * <code>{{missing: <i>key</i>}}</code>).
   * 
   * @param key
   *          message key
   * @param locale
   *          locale
   * @return localized message string.
   */
  public abstract String getMessage(String key, Locale locale);

  /**
   * Return the message for <code>key</code> in <code>locale</code> formatted
   * with <code>arguments</code> specified. If no message is defined for the
   * given <code>key</code> the key itself is returned potentially marked as
   * missing (e.g. <code>{{missing: <i>key</i>}}</code>).
   * 
   * @param key
   *          message key
   * @param args
   *          parameters for the message
   * @param locale
   *          locale
   * @return localized and formatted message string.
   */
  public abstract String getMessage(String key, Object[] args, Locale locale);

}
