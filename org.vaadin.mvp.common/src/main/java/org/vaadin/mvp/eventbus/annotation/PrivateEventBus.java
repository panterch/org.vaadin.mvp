package org.vaadin.mvp.eventbus.annotation;

import java.lang.annotation.*;

/**
 * If the event bus is annotated a private bus will be used, this will allow the usage of more instances of the same presenter
 * User: apalumbo
 * Date: 8/30/12
 * Time: 3:42 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target({ElementType.TYPE})
public @interface PrivateEventBus {
}
