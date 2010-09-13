package org.vaadin.mvp.presenter;

import org.vaadin.mvp.eventbus.EventBus;

/**
 * Interface that all presenters should implement.
 * 
 * @author tam
 *
 * @param <V> View type
 * @param <E> Event bus type
 */
public interface IPresenter<V, E extends EventBus> {

  public void setEventBus(E eventBus);
  
  public E getEventBus();
  
  public void setView(V view);

  public V getView();
  
  public void bind();
  
  public void bindIfNeeded();
  
}
