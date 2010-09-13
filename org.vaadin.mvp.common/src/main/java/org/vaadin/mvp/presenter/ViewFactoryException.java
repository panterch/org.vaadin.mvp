package org.vaadin.mvp.presenter;

public class ViewFactoryException extends Exception {

  public ViewFactoryException() {
  }

  public ViewFactoryException(String message) {
    super(message);
  }

  public ViewFactoryException(Throwable cause) {
    super(cause);
  }

  public ViewFactoryException(String message, Throwable cause) {
    super(message, cause);
  }

}
