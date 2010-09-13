package org.vaadin.mvp.uibinder;

public class UiBinderException extends Exception {

  public UiBinderException() {
  }

  public UiBinderException(String message) {
    super(message);
  }

  public UiBinderException(Throwable cause) {
    super(cause);
  }

  public UiBinderException(String message, Throwable cause) {
    super(message, cause);
  }

}
