package org.vaadin.mvp.eventbus;

/**
 * Created by IntelliJ IDEA.
 * User: apalumbo
 * Date: 8/30/12
 * Time: 6:30 PM
 */
public interface IEventReceiverRegistry {
  /**
   * Add a receiver to the registry.
   *
   * @param receiver Instance of the event receiver.
   */
  void addReceiver(Object receiver);

  /**
   * Lookup a receiver by its type.
   *
   * @param <T>
   *          Receiver type
   * @param receiverType
   *          Receiver type class
   * @return the receiver instance if present in the registry or
   *         <code>null</code>
   */
  <T> T lookupReceiver(Class<T> receiverType);
}
