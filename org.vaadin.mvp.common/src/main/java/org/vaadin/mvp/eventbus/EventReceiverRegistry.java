package org.vaadin.mvp.eventbus;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event handler registry holding <i>active</i> subscribers (i.e. the actual
 * event handlers) as weak references to allow collection in case no other
 * references are held by the application.
 * 
 * @author tam
 */
public class EventReceiverRegistry implements IEventReceiverRegistry {

  
  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(EventReceiverRegistry.class);
  
  /**
   * Event receiver registry using WeakReference to the receiver instances to
   * allow garbage collection of them. Note that a normal HashMap is used since
   * a WeakHashMap does only collect non-referenced keys - which are class
   * objects that will not be collected in our case.
   */
  private Map<Class<?>, WeakReference<?>> receivers = new HashMap<Class<?>, WeakReference<?>>();

  @Override
  public void addReceiver(Object receiver) {
    // clear collected receivers from our map first
    // create a "copy" of the maps keyset to allow modification while looping
    Set<Class<?>> keySet = new HashSet<Class<?>>(receivers.keySet());
    for (Class<?> receiverType : keySet) {
      WeakReference<?> reference = receivers.get(receiverType);
      if (reference.get() == null) {
        logger.debug("Removing mapping: {}", receiverType);
        receivers.remove(receiverType);
      }
    }
    receivers.put(receiver.getClass(), new WeakReference<Object>(receiver));
  }

  @Override
  public <T> T lookupReceiver(Class<T> receiverType) {
    if (receivers.containsKey(receiverType)) {
      WeakReference<T> reference = (WeakReference<T>) receivers.get(receiverType);
      T receiver = reference.get();
      // remove the entry if reference has been collected
      if(receiver == null) {
        logger.debug("Removing mapping: {}", receiverType);
        receivers.remove(receiverType);
      }
      return (T) receiver;
    }
    return null;
  }

}
