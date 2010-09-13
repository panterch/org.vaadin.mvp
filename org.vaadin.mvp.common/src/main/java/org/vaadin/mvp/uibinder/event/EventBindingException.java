package org.vaadin.mvp.uibinder.event;

public class EventBindingException extends Exception {

  public EventBindingException() {
  }

  public EventBindingException(String message, Throwable cause) {
    super(message, cause);
  }

  public EventBindingException(String message) {
    super(message);
  }

  public EventBindingException(Throwable cause) {
    super(cause);
  }
  
}
