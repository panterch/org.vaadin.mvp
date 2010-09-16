package org.vaadin.mvp.uibinder.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.uibinder.UiBinderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Binding parser handler is a SAX event handler that delegates all events to
 * {@link TargetHandler}s. Each {@link TargetHandler} is responsible for a
 * different namespace.
 * 
 * 
 * 
 * @author tam
 *
 */
public class BindingParserHandler extends DefaultHandler {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(BindingParserHandler.class);

  /** Map of handlers to namespaces */
  private Map<String, TargetHandler> handlers = new HashMap<String, TargetHandler>();

  /**
   * Constructor
   * 
   * @param targetHandlers
   */
  public BindingParserHandler(TargetHandler... targetHandlers) {
    handlers.put(null, new NoOpTargetHandler());
    for (TargetHandler handler : targetHandlers) {
      handlers.put(handler.getTargetNamespace(), handler);
    }
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    logger.debug("Start Element: {}", qName);
    try {
      TargetHandler handler = getHandler(uri);
      handler.handleElementOpen(uri, localName);
      for (int i = 0; i < attributes.getLength(); i++) {
        String attUri = attributes.getURI(i);
        if("".equals(attUri.trim())) {
          attUri = uri;
        }
        getHandler(attUri).handleAttribute(attributes.getLocalName(i), attributes.getValue(i));
      }
    } catch (UiBinderException e) {
      // FIXME: add document locator information
      throw new SAXException(e);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    logger.debug("End element: {}", qName);
    try {
      getHandler(uri).handleElementClose();
    } catch (UiBinderException e) {
      throw new SAXException(e);
    }
  }

  private TargetHandler getHandler(String uri) {
    if (handlers.containsKey(uri)) {
      return handlers.get(uri);
    }
    return handlers.get(null);
  }

  private class NoOpTargetHandler implements TargetHandler {

    public String getTargetNamespace() {
      return null;
    }

    @Override
    public void handleElementOpen(String uri, String name) {
    }

    @Override
    public void handleElementClose() {
    }

    @Override
    public void handleAttribute(String name, Object value) {
    }

  }

}
