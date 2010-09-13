package org.vaadin.mvp.eventbus.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Event {

  Class<?>[] handlers() default {};
  
  boolean forwardToParent() default false;
  
}
