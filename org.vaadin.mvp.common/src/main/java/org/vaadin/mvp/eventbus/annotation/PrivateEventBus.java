package org.vaadin.mvp.eventbus.annotation;

import java.lang.annotation.*;

/**
 * If the event bus is annotated a private bus will be used,
 * this will allow the usage of more instances of the same presenter
 *
 * @author : apalumbo
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target({ElementType.TYPE})
public @interface PrivateEventBus {
}
